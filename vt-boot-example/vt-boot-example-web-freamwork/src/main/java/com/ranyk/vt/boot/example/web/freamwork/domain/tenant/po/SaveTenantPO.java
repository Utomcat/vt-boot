package com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: SaveTenantPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 新增租户请求参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 租户名称, 不能为空</li>
 *     <li>code: 租户编码, 不能为空</li>
 *     <li>remark: 租户描述, 可为空</li>
 * </ul>
 * @date: 2026-04-06
 */
@Builder
public record SaveTenantPO(@Nonnull String name, @Nonnull String code, String remark) implements Serializable {
}
