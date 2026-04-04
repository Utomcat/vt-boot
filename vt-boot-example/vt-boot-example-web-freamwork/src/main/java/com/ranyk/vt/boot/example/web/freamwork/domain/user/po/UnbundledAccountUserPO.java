package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

/**
 * CLASS_NAME: UnbundledAccountUserPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 解绑账户和用户信息数据封装 PO 类, 属性说明: <br/>
 * <ul>
 *     <li>id: 账户用户关联关系数据ID</li>
 *     <li>userId: 用户ID</li>
 *     <li>accountId: 账户ID</li>
 * </ul>
 * @date: 2026-03-24
 */
public record UnbundledAccountUserPO(String id, String userId, String accountId) {
}
