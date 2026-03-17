package com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: QueryPermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询权限参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 权限名称</li>
 *     <li>code: 权限编码</li>
 *     <li>type: 权限类型（1: 菜单; 2: 按钮; 3: 功能; 4: 其他;）</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *     <li>remark: 描述, 默认值为空字符串</li>
 *     <li>currentPage: 当前页码, 默认值为1</li>
 *     <li>pageSize: 每页显示的记录数, 默认值为10</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryPermissionPO(String name, String code, Integer type, Integer status, String remark, Integer currentPage, Integer pageSize) implements Serializable {

    public QueryPermissionPO {
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
