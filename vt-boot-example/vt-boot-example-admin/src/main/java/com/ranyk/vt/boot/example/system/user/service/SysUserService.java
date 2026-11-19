package com.ranyk.vt.boot.example.system.user.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.util.PasswordUtils;
import com.ranyk.vt.boot.example.constant.RoleCodeEnum;
import com.ranyk.vt.boot.example.system.user.converter.SysUserConverter;
import com.ranyk.vt.boot.example.system.user.domain.dto.UserFormDTO;
import com.ranyk.vt.boot.example.system.user.domain.dto.UserQueryDTO;
import com.ranyk.vt.boot.example.system.user.domain.entity.SysUser;
import com.ranyk.vt.boot.example.system.user.domain.vo.UserVO;
import com.ranyk.vt.boot.example.system.user.mapper.SysUserMapper;
import com.ranyk.vt.boot.example.system.rbac.service.RbacService;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRole;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysUserRole;
import com.ranyk.vt.boot.example.system.role.mapper.SysRoleMapper;
import com.ranyk.vt.boot.example.system.role.mapper.SysUserRoleMapper;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: SysUserService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户管理服务
 * @date: 2026-11-16
 */
@Service
@RequiredArgsConstructor
public class SysUserService {

    /**
     * 默认角色
     */
    private static final String DEFAULT_ROLE = "USER";
    /**
     * 状态: 正常
     */
    private static final int STATUS_NORMAL = 1;

    /**
     * 用户数据访问对象
     */
    private final SysUserMapper sysUserMapper;
    /**
     * 用户实体转换器
     */
    private final SysUserConverter sysUserConverter;
    /**
     * 角色数据访问对象
     */
    private final SysRoleMapper sysRoleMapper;
    /**
     * 用户-角色关联数据访问对象
     */
    private final SysUserRoleMapper sysUserRoleMapper;
    /**
     * RBAC 权限解析服务, 用于在用户角色变更后失效权限缓存
     */
    private final RbacService rbacService;

    /**
     * 按用户名、状态、角色等条件分页查询用户。查询条件为空时自动忽略该条件,
     * 结果按创建时间倒序排列, 并转换成前端展示所需的用户视图对象。
     *
     * @param queryDTO 分页查询参数, 包含页码、每页条数及可选筛选条件
     * @return 包含当前页用户列表与总数、页码、页大小的分页结果
     */
    public MultiResult<UserVO> page(UserQueryDTO queryDTO) {
        Page<SysUser> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.<SysUser>lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getUsername()), SysUser::getUsername, queryDTO.getUsername())
                .eq(ObjectUtil.isNotNull(queryDTO.getStatus()), SysUser::getStatus, queryDTO.getStatus())
                .apply(StrUtil.isNotBlank(queryDTO.getRole()),
                        "id IN (SELECT ur.user_id FROM t_user_role ur JOIN t_role r ON ur.role_id = r.id WHERE r.code = {0})",
                        queryDTO.getRole())
                .orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> result = sysUserMapper.selectPage(page, wrapper);
        List<UserVO> list = result.getRecords().stream().map(sysUserConverter::toVO).collect(Collectors.toList());
        fillRoles(list);
        return MultiResult.successMulti(list, result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    /**
     * 根据用户ID查询用户详情, 用户不存在时抛出业务异常。
     *
     * @param id 用户ID
     * @return 用户详细信息
     */
    public UserVO detail(Long id) {
        UserVO userVO = sysUserConverter.toVO(getExistUser(id));
        fillRoles(List.of(userVO));
        return userVO;
    }

    /**
     * 新增用户。会校验用户名和密码必填、用户名全局唯一, 密码经 BCrypt 加密后存储;
     * 未指定角色和状态时分别使用默认角色 USER 和正常状态。
     *
     * @param formDTO 用户表单参数
     * @return 新增用户的ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(UserFormDTO formDTO) {
        if (StrUtil.isBlank(formDTO.getUsername())) {
            throw new ServiceException("用户名不能为空");
        }
        if (StrUtil.isBlank(formDTO.getPassword())) {
            throw new ServiceException("密码不能为空");
        }
        Long count = sysUserMapper.selectCount(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, formDTO.getUsername()));
        if (count > 0) {
            throw new ServiceException("用户名已存在");
        }
        // 表单转实体后, 补 齐密码加密与默认值
        SysUser user = sysUserConverter.toEntity(formDTO);
        user.setPassword(PasswordUtils.encode(formDTO.getPassword()));
        List<Long> roleIds = normalizeRoleIds(formDTO.getRoleIds());
        user.setRole(primaryRoleCode(roleIds));
        user.setStatus(ObjectUtil.isNull(formDTO.getStatus()) ? STATUS_NORMAL : formDTO.getStatus());
        user.setLoginFailCount(0);
        sysUserMapper.insert(user);
        syncUserRole(user.getId(), roleIds);
        return user.getId();
    }

    /**
     * 修改用户资料。仅更新非空字段, 密码统一由重置密码接口修改, 此处不再处理密码。
     *
     * @param formDTO 用户表单参数, 必须包含用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(UserFormDTO formDTO) {
        if (ObjectUtil.isNull(formDTO.getId())) {
            throw new ServiceException("用户ID不能为空");
        }
        SysUser user = getExistUser(formDTO.getId());
        sysUserConverter.updateEntity(formDTO, user);
        List<Long> roleIds = null;
        if (ObjectUtil.isNotNull(formDTO.getRoleIds())) {
            roleIds = normalizeRoleIds(formDTO.getRoleIds());
            user.setRole(primaryRoleCode(roleIds));
        }
        sysUserMapper.updateById(user);
        if (ObjectUtil.isNotNull(roleIds)) {
            syncUserRole(user.getId(), roleIds);
        }
    }

    /**
     * 逻辑删除用户。删除前先确认用户存在, 底层使用 MyBatis-Plus 逻辑删除,
     * 仅将 deleted 标记为 1, 不会物理删除用户及其历史数据。
     *
     * @param id 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getExistUser(id);
        sysUserMapper.deleteById(id);
        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, id));
        rbacService.evict(id);
    }

    /**
     * 重置用户密码。校验新密码非空后, 使用 BCrypt 加密并覆盖原密码。
     *
     * @param id          用户ID
     * @param newPassword 新密码明文
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Long id, String newPassword) {
        if (StrUtil.isBlank(newPassword)) {
            throw new ServiceException("新密码不能为空");
        }
        SysUser user = getExistUser(id);
        user.setPassword(PasswordUtils.encode(newPassword));
        sysUserMapper.updateById(user);
    }

    /**
     * 修改用户状态(启用/禁用)。状态值不能为空, 修改前会确认用户存在。
     *
     * @param id     用户ID
     * @param status 目标状态: 1-正常 0-禁用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        if (ObjectUtil.isNull(status)) {
            throw new ServiceException("状态不能为空");
        }
        SysUser user = getExistUser(id);
        user.setStatus(status);
        sysUserMapper.updateById(user);
    }

    /**
     * 同步用户的角色绑定: 先清除原有绑定, 再按角色ID写入新 的绑定关系。
     *
     * @param userId  用户ID
     * @param roleIds 角色ID集合
     */
    private void syncUserRole(Long userId, List<Long> roleIds) {
        sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        if (CollUtil.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                if (ObjectUtil.isNotNull(roleId)) {
                    sysUserRoleMapper.insert(new SysUserRole(userId, roleId));
                }
            }
        }
        rbacService.evict(userId);
    }

    /**
     * 规范化用户角色ID集合: 去重去空; 为空时回退到默认角色
     *
     * @param roleIds 原始角色ID集合
     * @return 规范化后的角色ID集合
     */
    private List<Long> normalizeRoleIds(List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            return roleIds.stream()
                    .filter(ObjectUtil::isNotNull)
                    .distinct()
                    .collect(Collectors.toList());
        }
        SysRole defaultRole = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery()
                .eq(SysRole::getCode, DEFAULT_ROLE)
                .last("LIMIT 1"));
        return ObjectUtil.isNull(defaultRole) ? new ArrayList<>() : new ArrayList<>(List.of(defaultRole.getId()));
    }

    /**
     * 根据角色ID集合解析用户主角色编码: 含管理员角色时取管理员, 否则取排序最靠前的角色编码, 兜底默认角色
     *
     * @param roleIds 角色ID集合
     * @return 主角色编码
     */
    private String primaryRoleCode(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return DEFAULT_ROLE;
        }
        List<SysRole> roles = sysRoleMapper.selectByIds(roleIds).stream()
                .sorted(Comparator.comparing(SysRole::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        boolean isAdmin = roles.stream()
                .anyMatch(role -> ObjectUtil.equal(RoleCodeEnum.ADMIN.getValue(), role.getCode()));
        if (isAdmin) {
            return RoleCodeEnum.ADMIN.getValue();
        }
        return roles.stream()
                .map(SysRole::getCode)
                .filter(StrUtil::isNotBlank)
                .findFirst()
                .orElse(DEFAULT_ROLE);
    }

    /**
     * 批量填充用户视图对象中的角色信息(角色ID、编码、名称)
     *
     * @param users 用户视图对象集合
     */
    private void fillRoles(List<UserVO> users) {
        if (CollUtil.isEmpty(users)) {
            return;
        }
        List<Long> userIds = users.stream()
                .map(UserVO::getId)
                .filter(ObjectUtil::isNotNull)
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        List<SysUserRole> bindings = sysUserRoleMapper.selectList(
                Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, userIds));
        Map<Long, List<Long>> userRoleMap = new HashMap<>();
        for (SysUserRole binding : bindings) {
            userRoleMap.computeIfAbsent(binding.getUserId(), key -> new ArrayList<>()).add(binding.getRoleId());
        }
        List<Long> allRoleIds = bindings.stream()
                .map(SysUserRole::getRoleId)
                .filter(ObjectUtil::isNotNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, SysRole> roleMap = new HashMap<>();
        if (CollUtil.isNotEmpty(allRoleIds)) {
            for (SysRole role : sysRoleMapper.selectByIds(allRoleIds)) {
                roleMap.put(role.getId(), role);
            }
        }
        for (UserVO vo : users) {
            List<Long> roleIds = userRoleMap.getOrDefault(vo.getId(), new ArrayList<>());
            List<SysRole> roles = roleIds.stream()
                    .map(roleMap::get)
                    .filter(ObjectUtil::isNotNull)
                    .sorted(Comparator.comparing(SysRole::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();
            vo.setRoleIds(roleIds);
            vo.setRoleCodes(roles.stream().map(SysRole::getCode).collect(Collectors.toList()));
            vo.setRoleNames(roles.stream().map(SysRole::getName).collect(Collectors.toList()));
        }
    }

    /**
     * 获取存在的用户
     *
     * @param id 用户ID
     * @return 用户实体 {@link SysUser}
     */
    private SysUser getExistUser(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException("用户不存在");
        }
        return user;
    }
}
