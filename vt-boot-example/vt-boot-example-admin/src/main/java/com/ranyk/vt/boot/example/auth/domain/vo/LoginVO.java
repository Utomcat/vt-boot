package com.ranyk.vt.boot.example.auth.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录成功返回结果
 * @date: 2026-11-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2499384512064002245L;

    /**
     * 访问令牌
     */
    private String token;
    /**
     * 令牌请求头名称
     */
    private String tokenName;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 角色
     */
    private String role;
    /**
     * 租户ID
     */
    private Long tenantId;
}
