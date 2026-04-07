package com.ranyk.vt.boot.example.web.freamwork.domain.tenant.vo;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryTenantVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户信息数据查询结果返回视图 VO 类, 字段说明:<br/>
 * <ul>
 *     <li>id: 租户数据ID</li>
 *     <li>name: 租户名称</li>
 *     <li>code: 租户编码</li>
 *     <li>remark: 租户描述</li>
 * </ul>
 * @date: 2026-04-06
 */
@Builder
public record QueryTenantVO(String id, String name, String code, String remark) implements Serializable {
}
