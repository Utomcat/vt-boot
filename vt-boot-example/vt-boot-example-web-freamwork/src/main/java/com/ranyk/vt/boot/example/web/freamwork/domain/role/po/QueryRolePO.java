package com.ranyk.vt.boot.example.web.freamwork.domain.role.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: QueryRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询角色参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 角色名称</li>
 *     <li>code: 角色编码</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *     <li>remark: 备注</li>
 *     <li>currentPage: 当前页码, 默认值为 1</li>
 *     <li>pageSize: 每页显示数量, 默认值为 10</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryRolePO(String name, String code, Integer status, String remark, Integer currentPage, Integer pageSize) implements Serializable {

    public QueryRolePO {
        if (Objects.isNull(status)) {
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
