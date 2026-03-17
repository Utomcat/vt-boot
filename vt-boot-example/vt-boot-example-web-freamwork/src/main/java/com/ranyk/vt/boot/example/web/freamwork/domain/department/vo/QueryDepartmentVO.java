package com.ranyk.vt.boot.example.web.freamwork.domain.department.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryDepartmentVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询部门信息返回对象 VO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 部门ID</li>
 *     <li>name: 部门名称</li>
 *     <li>code: 部门编码</li>
 *     <li>parentId: 父级部门ID</li>
 *     <li>parentIds: 父级部门ID列表, 逗号分隔</li>
 *     <li>remark: 备注, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryDepartmentVO(@Nonnull String id, String name, String code, String parentId, String parentIds, String remark) implements Serializable {

}
