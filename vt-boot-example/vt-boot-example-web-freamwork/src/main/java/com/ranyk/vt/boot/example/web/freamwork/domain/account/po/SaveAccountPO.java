package com.ranyk.vt.boot.example.web.freamwork.domain.account.po;

import jakarta.annotation.Nonnull;
import lombok.*;

import java.io.Serializable;

/**
 * CLASS_NAME: SaveAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 新增账户信息参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>userName: 用户名, 不能为空</li>
 *     <li>password: 密码, 不能为空</li>
 *     <li>remark: 账户备注</li>
 *     <li>departmentId: 部门ID</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record SaveAccountPO(@Nonnull String userName, @Nonnull String password, String remark, String departmentId) implements Serializable {
}
