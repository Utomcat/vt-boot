package com.ranyk.vt.boot.example.satoken.domain.account.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录账户信息数据封装对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class LoginAccountPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5285512224146018250L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String captcha;
}
