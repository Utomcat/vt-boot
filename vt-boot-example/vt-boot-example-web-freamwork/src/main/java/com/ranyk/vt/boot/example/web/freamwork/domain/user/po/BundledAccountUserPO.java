package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: BundledAccountUserPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 绑定账户用户信息关联关系 PO 类, 属性说明:<br/>
 * <ul>
 *     <li>userId: 用户ID</li>
 *     <li>accountId: 账户ID</li>
 * </ul>
 * @date: 2026-03-24
 */
@Builder
public record BundledAccountUserPO(@Nonnull String userId, @Nonnull String accountId) implements Serializable {
}
