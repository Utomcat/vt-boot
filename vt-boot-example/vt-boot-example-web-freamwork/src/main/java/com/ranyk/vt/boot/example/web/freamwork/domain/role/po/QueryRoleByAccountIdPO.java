package com.ranyk.vt.boot.example.web.freamwork.domain.role.po;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryRoleByAccountIdPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入通过账户ID查询角色信息数据封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>accountId: 账户ID</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record QueryRoleByAccountIdPO(String accountId) implements Serializable {
}
