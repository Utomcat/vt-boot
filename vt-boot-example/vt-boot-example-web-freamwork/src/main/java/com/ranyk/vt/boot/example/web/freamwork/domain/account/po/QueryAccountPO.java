package com.ranyk.vt.boot.example.web.freamwork.domain.account.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: QueryAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询账户信息参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>userName: 用户名</li>
 *     <li>remark: 账户备注</li>
 *     <li>status: 账户状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态(默认值);）</li>
 *     <li>departmentId: 部门ID</li>
 *     <li>currentPage: 当前页, 默认为 1</li>
 *     <li>pageSize: 每页显示数量, 默认为 10</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryAccountPO(String userName, String remark, Integer status, String departmentId, Integer currentPage, Integer pageSize) implements Serializable {

    public QueryAccountPO {
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
