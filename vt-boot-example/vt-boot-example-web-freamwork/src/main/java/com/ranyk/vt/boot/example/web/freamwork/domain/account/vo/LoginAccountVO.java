package com.ranyk.vt.boot.example.web.freamwork.domain.account.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: LoginAccountVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户登录信息返回前端数据封装类, 字段说明: <br/>
 * <ul>
 *     <li>token: 登录凭证, 不能为空</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record LoginAccountVO(@Nonnull String token) implements Serializable {

}
