package com.ranyk.vt.boot.example.auth.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: RefreshTokenDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 刷新令牌请求参数
 * @date: 2026-11-16
 */
@Data
public class RefreshTokenDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2975149938850310011L;

    /**
     * 刷新令牌
     */
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}
