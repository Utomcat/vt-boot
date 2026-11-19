package com.ranyk.vt.boot.example.auth.service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ranyk.vt.boot.base.constant.UserErrorCodeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.exception.UserException;
import com.ranyk.vt.boot.base.util.PasswordUtils;
import com.ranyk.vt.boot.example.auth.domain.dto.LoginDTO;
import com.ranyk.vt.boot.example.auth.domain.vo.LoginVO;
import com.ranyk.vt.boot.example.auth.domain.vo.UserInfoVO;
import com.ranyk.vt.boot.example.auth.domain.vo.UserMenuVO;
import com.ranyk.vt.boot.base.constant.SessionKeyEnum;
import com.ranyk.vt.boot.example.system.log.domain.entity.LoginLog;
import com.ranyk.vt.boot.example.system.rbac.service.RbacService;import com.ranyk.vt.boot.example.system.log.mapper.LoginLogMapper;
import com.ranyk.vt.boot.example.system.tenant.domain.entity.SysTenant;
import com.ranyk.vt.boot.example.system.tenant.mapper.SysTenantMapper;
import com.ranyk.vt.boot.example.system.token.domain.entity.UserRefreshToken;
import com.ranyk.vt.boot.example.system.token.mapper.UserRefreshTokenMapper;
import com.ranyk.vt.boot.example.system.user.converter.SysUserConverter;
import com.ranyk.vt.boot.example.system.user.domain.entity.SysUser;
import com.ranyk.vt.boot.example.system.user.mapper.SysUserMapper;
import com.ranyk.vt.boot.satoken.config.properties.SaTokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * CLASS_NAME: AuthService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 认证服务, 负责登录校验、Sa-Token 会话建立、刷新令牌签发与登录审计
 * @date: 2026-11-16
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * 正常状态
     */
    private static final int STATUS_NORMAL = 1;
    /**
     * 登录方式: 密码
     */
    private static final String LOGIN_TYPE_PASSWORD = "PASSWORD";
    /**
     * 登录方式: 刷新令牌
     */
    private static final String LOGIN_TYPE_REFRESH_TOKEN = "REFRESH_TOKEN";
    /**
     * 允许的最大连续登录失败次数
     */
    private static final int MAX_LOGIN_FAIL_COUNT = 5;
    /**
     * 账号锁定时长(分钟)
     */
    private static final int LOCK_MINUTES = 15;
    /**
     * 刷新令牌有效期(天)
     */
    private static final int REFRESH_TOKEN_EXPIRE_DAYS = 7;

    /**
     * 用户数据访问对象
     */
    private final SysUserMapper sysUserMapper;
    /**
     * 刷新令牌数据访问对象
     */
    private final UserRefreshTokenMapper userRefreshTokenMapper;
    /**
     * 登录日志数据访问对象
     */
    private final LoginLogMapper loginLogMapper;
    /**
     * Sa-Token 配置属性
     */
    private final SaTokenProperties saTokenProperties;
    /**
     * 用户实体转换器
     */
    private final SysUserConverter sysUserConverter;
    /**
     * RBAC 权限解析服务
     */
    private final RbacService rbacService;
    /**
     * 租户数据访问对象, 用于回填租户名称
     */
    private final SysTenantMapper sysTenantMapper;

    /**
     * 校验用户名和密码, 建立 Sa-Token 会话, 签发刷新令牌, 并记录本次登录结果。
     * 登录失败时会区分账号不存在、账号禁用、账号锁定和密码错误, 密码连续错误达到阈值后会暂时锁定账号。
     *
     * @param loginDTO 登录请求参数, 包含用户名、密码和设备信息
     * @param ip       客户端IP地址
     * @return 包含访问令牌、刷新令牌和用户基本信息的登录结果
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginDTO loginDTO, String ip) {
        String username = loginDTO.getUsername();
        SysUser user = sysUserMapper.selectOne(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        if (ObjectUtil.isNull(user)) {
            saveLoginLog(null, username, LOGIN_TYPE_PASSWORD, Boolean.FALSE, "USER_NOT_FOUND", ip, loginDTO.getDeviceInfo());
            throw new UserException(UserErrorCodeEnum.NOT_EXISTS.getValue(), new Object[]{username});
        }
        if (!ObjectUtil.equal(STATUS_NORMAL, user.getStatus())) {
            saveLoginLog(user.getId(), username, LOGIN_TYPE_PASSWORD, Boolean.FALSE, "DISABLED", ip, loginDTO.getDeviceInfo());
            throw new ServiceException("账号已被禁用, 请联系管理员");
        }
        if (ObjectUtil.isNotNull(user.getLockTime()) && user.getLockTime().isAfter(LocalDateTime.now())) {
            saveLoginLog(user.getId(), username, LOGIN_TYPE_PASSWORD, Boolean.FALSE, "LOCKED", ip, loginDTO.getDeviceInfo());
            throw new ServiceException("账号已被锁定, 请稍后再试");
        }
        if (!PasswordUtils.matches(loginDTO.getPassword(), user.getPassword())) {
            handleLoginFail(user, ip, loginDTO.getDeviceInfo());
            throw new UserException(UserErrorCodeEnum.PASSWORD_NOT_MATCH.getValue(), new Object[]{});
        }
        // 登录成功: 重置失败次数与锁定时间, 更新最后登录信息
        user.setLoginFailCount(0);
        user.setLockTime(null);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ip);
        sysUserMapper.updateById(user);
        // 建立登录会话
        establishSession(user);
        UserRefreshToken refreshToken = createRefreshToken(user, ip, loginDTO.getDeviceInfo());
        saveLoginLog(user.getId(), username, LOGIN_TYPE_PASSWORD, Boolean.TRUE, null, ip, loginDTO.getDeviceInfo());
        return buildLoginVO(user, refreshToken.getRefreshToken());
    }

    /**
     * 使用刷新令牌换取新的访问令牌。校验刷新令牌是否存在、是否被撤销、是否过期, 校验通过后执行令牌轮换:
     * 将旧刷新令牌置为已撤销, 再签发新的访问令牌与刷新令牌, 从而降低刷新令牌被重复使用的风险。
     *
     * @param refreshToken 客户端提交的刷新令牌
     * @param ip           客户端IP地址
     * @return 包含新访问令牌、新刷新令牌和用户基本信息的登录结果
     */
    @Transactional(rollbackFor = Exception.class)
    public LoginVO refresh(String refreshToken, String ip) {
        UserRefreshToken token = userRefreshTokenMapper.selectOne(
                Wrappers.<UserRefreshToken>lambdaQuery().eq(UserRefreshToken::getRefreshToken, refreshToken));
        if (ObjectUtil.isNull(token) || ObjectUtil.equal(token.getRevoked(), 1)
                || ObjectUtil.isNull(token.getExpireTime()) || token.getExpireTime().isBefore(LocalDateTime.now())) {
            saveLoginLog(null, "-", LOGIN_TYPE_REFRESH_TOKEN, Boolean.FALSE, "INVALID_REFRESH_TOKEN", ip, null);
            throw new ServiceException("刷新令牌无效或已过期, 请重新登录");
        }
        SysUser user = sysUserMapper.selectById(token.getUserId());
        if (ObjectUtil.isNull(user) || !ObjectUtil.equal(STATUS_NORMAL, user.getStatus())) {
            throw new ServiceException("账号不存在或已被禁用");
        }
        // 旧刷新令牌作废, 签发新的刷新令牌(令牌轮换)
        token.setRevoked(1);
        userRefreshTokenMapper.updateById(token);
        establishSession(user);
        UserRefreshToken newToken = createRefreshToken(user, ip, token.getDeviceInfo());
        saveLoginLog(user.getId(), user.getUsername(), LOGIN_TYPE_REFRESH_TOKEN, Boolean.TRUE, null, ip, token.getDeviceInfo());
        return buildLoginVO(user, newToken.getRefreshToken());
    }

    /**
     * 退出当前登录会话, 并将该用户仍在有效期内的刷新令牌全部置为已撤销,
     * 避免退出登录后旧刷新令牌还能继续换取访问令牌。
     */
    public void logout() {
        if (!StpUtil.isLogin()) {
            return;
        }
        Long userId = Convert.toLong(StpUtil.getLoginIdAsString());
        StpUtil.logout();
        // 撤销该用户全部刷新令牌
        UserRefreshToken update = new UserRefreshToken();
        update.setRevoked(1);
        userRefreshTokenMapper.update(update,
                Wrappers.<UserRefreshToken>lambdaUpdate().eq(UserRefreshToken::getUserId, userId));
    }

    /**
     * 获取当前登录用户的详细信息。会话中只保存了用户标识, 这里回查数据库获取最新的昵称、邮箱、
     * 手机号和头像等资料, 保证用户资料被修改后能实时反映到前端。
     *
     * @return 当前登录用户的详细信息
     */
    public UserInfoVO currentUserInfo() {
        Long userId = Convert.toLong(StpUtil.getLoginIdAsString());
        SysUser user = sysUserMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        UserInfoVO userInfo = sysUserConverter.toUserInfo(user);
        if (ObjectUtil.isNotNull(user.getTenantId())) {
            SysTenant tenant = sysTenantMapper.selectById(user.getTenantId());
            if (ObjectUtil.isNotNull(tenant)) {
                userInfo.setTenantName(tenant.getName());
            }
        }
        return userInfo;
    }

    /**
     * 获取当前登录用户的角色编码、权限码与可见菜单树。会话中只保存用户标识, 这里实时解析,
     * 保证角色或菜单调整后能尽快生效。
     *
     * @return 当前登录用户的角色、权限与菜单树
     */
    public UserMenuVO currentUserMenus() {
        Long userId = Convert.toLong(StpUtil.getLoginIdAsString());
        SysUser user = sysUserMapper.selectById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        return UserMenuVO.builder()
                .roles(rbacService.roleCodes(userId))
                .permissions(rbacService.permissions(userId))
                .menus(rbacService.menuTree(userId))
                .build();
    }

    /**
     * 处理登录失败: 累计失败次数, 达到阈值后锁定账号, 记录审计日志
     *
     * @param user       用户
     * @param ip         客户端IP
     * @param deviceInfo 设备信息
     */
    private void handleLoginFail(SysUser user, String ip, String deviceInfo) {
        int failCount = ObjectUtil.isNull(user.getLoginFailCount()) ? 1 : user.getLoginFailCount() + 1;
        user.setLoginFailCount(failCount);
        if (failCount >= MAX_LOGIN_FAIL_COUNT) {
            user.setLockTime(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
        }
        sysUserMapper.updateById(user);
        saveLoginLog(user.getId(), user.getUsername(), LOGIN_TYPE_PASSWORD, Boolean.FALSE, "BAD_CREDENTIALS", ip, deviceInfo);
    }

    /**
     * 建立 Sa-Token 登录会话并写入会话属性
     *
     * @param user 用户
     */
    private void establishSession(SysUser user) {
        StpUtil.login(user.getId());
        SaSession session = StpUtil.getSession();
        session.set(SessionKeyEnum.TENANT_ID.getValue(), Convert.toStr(user.getTenantId()));
        session.set(SessionKeyEnum.USERNAME.getValue(), user.getUsername());
        session.set(SessionKeyEnum.REAL_NAME.getValue(), user.getRealName());
        session.set(SessionKeyEnum.ROLE.getValue(), user.getRole());
    }

    /**
     * 创建并持久化刷新令牌
     *
     * @param user       用户
     * @param ip         客户端IP
     * @param deviceInfo 设备信息
     * @return 刷新令牌实体 {@link UserRefreshToken}
     */
    private UserRefreshToken createRefreshToken(SysUser user, String ip, String deviceInfo) {
        UserRefreshToken token = new UserRefreshToken();
        token.setUserId(user.getId());
        token.setRefreshToken(IdUtil.fastSimpleUUID());
        token.setDeviceInfo(deviceInfo);
        token.setIp(ip);
        token.setExpireTime(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAYS));
        token.setRevoked(0);
        token.setCreateTime(LocalDateTime.now());
        userRefreshTokenMapper.insert(token);
        return token;
    }

    /**
     * 构建登录返回结果
     *
     * @param user         用户
     * @param refreshToken 刷新令牌
     * @return 登录结果 {@link LoginVO}
     */
    private LoginVO buildLoginVO(SysUser user, String refreshToken) {
        return LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .tokenName(saTokenProperties.getTokenName())
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .role(user.getRole())
                .tenantId(user.getTenantId())
                .build();
    }

    /**
     * 保存登录审计日志
     *
     * @param userId     用户ID
     * @param username   登录名
     * @param loginType  登录方式
     * @param success    是否成功
     * @param failReason 失败原因
     * @param ip         客户端IP
     * @param deviceInfo 设备信息
     */
    private void saveLoginLog(Long userId, String username, String loginType, Boolean success,
                              String failReason, String ip, String deviceInfo) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLog.setUsername(StrUtil.blankToDefault(username, "-"));
        loginLog.setLoginType(loginType);
        loginLog.setSuccess(Boolean.TRUE.equals(success) ? 1 : 0);
        loginLog.setFailReason(failReason);
        loginLog.setIp(ip);
        loginLog.setDeviceInfo(deviceInfo);
        loginLog.setCreateTime(LocalDateTime.now());
        loginLogMapper.insert(loginLog);
    }
}
