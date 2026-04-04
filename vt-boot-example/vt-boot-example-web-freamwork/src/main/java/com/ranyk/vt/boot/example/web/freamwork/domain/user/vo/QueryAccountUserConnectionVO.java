package com.ranyk.vt.boot.example.web.freamwork.domain.user.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryAccountUserConnectionVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户信息查询结果封装视图 VO 对象, 字段说明: <br/>
 * <ul>
 *     <li>id: 账户用户关联关系数据主键 ID</li>
 *     <li>userId: 用户ID</li>
 *     <li>userName: 用户名称</li>
 *     <li>accountId: 账户ID</li>
 *     <li>accountName: 账户名称</li>
 * </ul>
 * @date: 2026-03-26
 */
@Builder
public record QueryAccountUserConnectionVO(@Nonnull String id, @Nonnull String userId, String userName, @Nonnull String accountId, String accountName) implements Serializable {
}
