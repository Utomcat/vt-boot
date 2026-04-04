package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: QueryUserPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 通过账户ID查询用户信息请求数据封装 PO 类, 字段说明:<br/>
 * <ul>
 *     <li>name: 用户名</li>
 *     <li>nickName: 昵称</li>
 *     <li>sex: 性别</li>
 *     <li>email: 邮箱</li>
 *     <li>phone: 手机号</li>
 *     <li>status: 状态</li>
 *     <li>remark: 备注</li>
 *     <li>currentPage: 当前页码</li>
 *     <li>pageSize: 每页显示数量</li>
 * </ul>
 * @date: 2026-03-17
 */
@Builder
public record QueryUserPO(String name, String nickName, Integer sex, String email, String phone, Integer status, String remark, Integer currentPage, Integer pageSize) implements Serializable {

    public QueryUserPO {
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
