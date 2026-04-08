package com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryPermissionByAccountIdPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入通过账户ID查询权限信息数据封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>accountId: 账户ID</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record QueryPermissionByAccountIdPO(String accountId) implements Serializable {
}
