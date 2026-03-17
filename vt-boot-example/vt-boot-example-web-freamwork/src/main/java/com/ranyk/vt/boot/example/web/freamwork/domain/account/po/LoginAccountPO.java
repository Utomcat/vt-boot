package com.ranyk.vt.boot.example.web.freamwork.domain.account.po;

import jakarta.annotation.Nonnull;
import lombok.*;

import java.io.Serializable;

/**
 * CLASS_NAME: LoginAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录账户信息数据封装对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>userName: 用户名, 不能为空</li>
 *     <li>password: 密码, 不能为空</li>
 *     <li>captcha: 验证码, 不能为空</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record LoginAccountPO(@Nonnull String userName, @Nonnull String password, @Nonnull String captcha) implements Serializable {

}
