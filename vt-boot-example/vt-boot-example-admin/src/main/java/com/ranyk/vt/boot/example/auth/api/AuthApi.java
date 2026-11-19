package com.ranyk.vt.boot.example.auth.api;

import cn.hutool.core.util.StrUtil;
import com.ranyk.vt.boot.example.auth.converter.AuthConverter;
import com.ranyk.vt.boot.example.auth.domain.po.LoginPO;
import com.ranyk.vt.boot.example.auth.domain.po.RefreshTokenPO;
import com.ranyk.vt.boot.example.auth.domain.vo.LoginVO;
import com.ranyk.vt.boot.example.auth.domain.vo.UserInfoVO;
import com.ranyk.vt.boot.example.auth.domain.vo.UserMenuVO;
import com.ranyk.vt.boot.example.auth.service.AuthService;
import com.ranyk.vt.boot.web.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: AuthApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 认证接口, 提供登录、刷新令牌、退出登录与获取当前用户信息等能力, 登录与刷新无需鉴权
 * @date: 2026-11-16
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApi {

    /**
     * 认证服务
     */
    private final AuthService authService;
    /**
     * 认证入参转换器
     */
    private final AuthConverter authConverter;

    /**
     * 账号密码登录。校验用户名与密码, 成功后建立登录会话并返回访问令牌、刷新令牌和用户基本信息;
     * 失败时按账号不存在、账号禁用、账号锁定或密码错误返回对应提示, 并记录登录审计日志。
     *
     * @param loginPO 登录请求参数, 包含用户名、密码和可选的设备信息
     * @param request 当前 HTTP 请求, 用于解析客户端真实 IP
     * @return 登录结果, 包含访问令牌、刷新令牌及用户基本信息
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginPO loginPO, HttpServletRequest request) {
        return Result.success(authService.login(authConverter.toLoginDTO(loginPO), getClientIp(request)));
    }

    /**
     * 刷新访问令牌。使用客户端提交的刷新令牌换取新的访问令牌, 同时作废旧刷新令牌并签发新的刷新令牌,
     * 以降低刷新令牌被重复使用的风险; 刷新令牌无效或过期时提示重新登录。
     *
     * @param refreshTokenPO 刷新令牌请求参数
     * @param request        当前 HTTP 请求, 用于解析客户端真实 IP
     * @return 新的登录结果, 包含新的访问令牌、刷新令牌及用户基本信息
     */
    @PostMapping("/refresh")
    public Result<LoginVO> refresh(@Valid @RequestBody RefreshTokenPO refreshTokenPO, HttpServletRequest request) {
        return Result.success(
                authService.refresh(authConverter.toRefreshTokenDTO(refreshTokenPO).getRefreshToken(), getClientIp(request)));
    }

    /**
     * 退出登录。注销当前登录会话, 并撤销该用户仍在有效期内的全部刷新令牌。
     *
     * @return 操作结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success(null);
    }

    /**
     * 获取当前登录用户信息。根据当前会话中的用户标识回查数据库, 返回最新的用户资料。
     *
     * @return 当前登录用户的详细信息
     */
    @GetMapping("/me")
    public Result<UserInfoVO> me() {
        return Result.success(authService.currentUserInfo());
    }

    /**
     * 获取当前登录用户的角色、权限与可见菜单树, 供前端动态生成路由与按钮权限。
     *
     * @return 当前登录用户的角色、权限与菜单树
     */
    @GetMapping("/menus")
    public Result<UserMenuVO> menus() {
        return Result.success(authService.currentUserMenus());
    }

    /**
     * 解析客户端真实 IP。依次读取常见的反向代理请求头, 取第一个非 unknown 的地址作为客户端 IP,
     * 均不存在时回退到 TCP 连接的远端地址。
     *
     * @param request 当前 HTTP 请求
     * @return 客户端 IP 地址
     */
    private String getClientIp(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (StrUtil.isNotBlank(ip) && !StrUtil.equalsIgnoreCase(ip, "unknown")) {
                return StrUtil.subBefore(ip, ',', false).trim();
            }
        }
        return request.getRemoteAddr();
    }
}
