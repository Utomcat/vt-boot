package com.ranyk.vt.boot.example.auth.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UserInfoVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 当前登录用户信息
 * @date: 2026-11-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6541788217491810341L;

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
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String avatar;
}
