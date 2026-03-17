package com.ranyk.vt.boot.example.web.freamwork.domain.department.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: UpdateDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 更新部门信息参数对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 部门ID</li>
 *     <li>name: 部门名称</li>
 *     <li>code: 部门编码</li>
 *     <li>parentId: 父级部门ID</li>
 *     <li>parentIds: 父级部门ID列表, 逗号分隔</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *     <li>remark: 备注, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record UpdateDepartmentPO(String id, String name, String code, String parentId, String parentIds, Integer status, String remark) implements Serializable {

    public UpdateDepartmentPO {
        if (Objects.isNull(status)) {
            status = 1;
        }
    }
}
