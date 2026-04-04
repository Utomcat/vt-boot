package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: QueryAccountUserConnectionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询账户用户关联关系信息参数数据封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>userName: 用户名称, 用于模糊匹配查询</li>
 *     <li>accountName: 账户名称, 用于模糊匹配查询</li>
 *     <li>currentPage: 当前页码, 默认值为 1</li>
 *     <li>pageSize: 每页显示数量, 默认值为 10</li>
 * </ul>
 * @date: 2026-03-26
 */
@Builder
public record QueryAccountUserConnectionPO(String userName, String accountName, Integer currentPage, Integer pageSize) implements Serializable {

    public QueryAccountUserConnectionPO {
        if (Objects.isNull(currentPage)) {
            currentPage = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = 10;
        }
    }
}
