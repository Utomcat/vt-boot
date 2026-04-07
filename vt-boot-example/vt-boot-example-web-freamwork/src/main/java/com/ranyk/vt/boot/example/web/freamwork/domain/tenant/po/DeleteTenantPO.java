package com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteTenantPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除租户请求参数封装 PO 类, 字段说明 : <br/>
 * <ul>
 *     <li>id: 租户ID</li>
 *     <li>ids: 租户ID列表</li>
 * </ul>
 * @date: 2026-04-06
 */
@Builder
public record DeleteTenantPO(String id, List<String>ids) implements Serializable {
}
