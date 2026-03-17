package com.ranyk.vt.boot.example.web.freamwork.domain.department.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: QueryDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询部门信息参数对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 部门名称</li>
 *     <li>code: 部门编码</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态(默认值);）</li>
 *     <li>remark: 备注, 默认值为空字符串</li>
 *     <li>currentPage: 当前页, 默认为 1</li>
 *     <li>pageSize: 每页显示数量, 默认为 10</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryDepartmentPO(String name, String code, Integer status, String remark, Integer currentPage, Integer pageSize) implements Serializable {

    public QueryDepartmentPO {
        if (Objects.isNull(status)){
            status = 1;
        }
        if (Objects.isNull(currentPage)) {
            currentPage = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = 10;
        }
    }

}
