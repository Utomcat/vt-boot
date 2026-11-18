package com.ranyk.vt.boot.example.auth.domain.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: RefreshTokenPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 刷新令牌接口入参
 * @date: 2026-11-16
 */
@Data
public class RefreshTokenPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3884127470176741965L;

    /**
     * 刷新令牌
     */
    @NotBlank(message = "刷新令牌不能为空")
    private String refreshToken;
}
