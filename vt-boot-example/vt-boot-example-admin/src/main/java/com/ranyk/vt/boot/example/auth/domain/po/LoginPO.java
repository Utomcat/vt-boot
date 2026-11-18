package com.ranyk.vt.boot.example.auth.domain.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录接口入参
 * @date: 2026-11-16
 */
@Data
public class LoginPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2477790746892781136L;

    /**
     * 登录名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 设备信息(浏览器/APP标识)
     */
    private String deviceInfo;
}
