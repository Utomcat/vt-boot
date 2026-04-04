package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryUserAccountIdPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 通过账户ID查询用户信息查询请求数据封装 PO 类, 字段说明:<br/>
 * <ul>
 *     <li>accountId: 账户ID</li>
 * </ul>
 * @date: 2026-03-24
 */
@Builder
public record QueryUserByAccountIdPO(String accountId) implements Serializable {
}
