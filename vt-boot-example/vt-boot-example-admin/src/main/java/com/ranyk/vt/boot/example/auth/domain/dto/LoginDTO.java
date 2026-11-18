package com.ranyk.vt.boot.example.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录请求参数
 * @date: 2026-11-16
 */
@Data
public class LoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5140184647091952123L;

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
