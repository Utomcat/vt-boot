package com.ranyk.vt.boot.example.web.freamwork.domain.department.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除部门信息参数对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 部门ID</li>
 *     <li>ids: 部门ID列表</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record DeleteDepartmentPO(String id, List<String> ids) implements Serializable {
}
