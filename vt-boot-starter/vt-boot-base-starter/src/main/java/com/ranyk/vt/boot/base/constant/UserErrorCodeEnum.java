package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: UserErrorCodeEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户相关错误码枚举类, 枚举值同时作为国际化消息的 Key 使用
 * @date: 2026-10-20
 */
@Getter
public enum UserErrorCodeEnum {
    /**
     * 用户未登录
     */
    NOT_LOGIN("user.not.login"),
    /**
     * 验证码错误
     */
    CAPTCHA_ERROR("user.captcha.error"),
    /**
     * 验证码已失效
     */
    CAPTCHA_EXPIRE("user.captcha.expire"),
    /**
     * 用户不存在
     */
    NOT_EXISTS("user.not.exists"),
    /**
     * 用户名或密码错误
     */
    PASSWORD_NOT_MATCH("user.password.not.match"),
    /**
     * 用户名不能为空
     */
    USERNAME_NOT_BLANK("user.username.not.blank"),
    /**
     * 用户名已存在
     */
    USERNAME_EXISTS("user.username.exists"),
    /**
     * 密码不能为空
     */
    PASSWORD_NOT_BLANK("user.password.not.blank"),
    ;

    /**
     * 错误码值 - 同时作为国际化消息的 Key
     */
    private final String value;

    /**
     * 构造函数
     *
     * @param value 错误码值
     */
    UserErrorCodeEnum(String value) {
        this.value = value;
    }
}
