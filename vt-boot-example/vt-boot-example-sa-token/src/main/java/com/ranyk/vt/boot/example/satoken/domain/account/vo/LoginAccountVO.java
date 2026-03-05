package com.ranyk.vt.boot.example.satoken.domain.account.vo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginAccountVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户登录信息返回前端数据封装类
 * @date: 2026-03-05
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class LoginAccountVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2215793273055058938L;

    /**
     * 登录成功返回的 token 数据值
     */
    private String token;
}
