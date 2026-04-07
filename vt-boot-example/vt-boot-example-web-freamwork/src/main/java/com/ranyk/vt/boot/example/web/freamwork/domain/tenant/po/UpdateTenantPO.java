package com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po;

import jakarta.annotation.Nonnull;

/**
 * CLASS_NAME: UpdateTenantPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 更新租户请求参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 租户数据ID, 不能为空</li>
 *     <li>name: 租户名称</li>
 *     <li>code: 租户编码</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *     <li>remark: 描述</li>
 * </ul>
 * @date: 2026-04-06
 */
public record UpdateTenantPO(@Nonnull String id, String name, String code, Integer status, String remark) {
}
