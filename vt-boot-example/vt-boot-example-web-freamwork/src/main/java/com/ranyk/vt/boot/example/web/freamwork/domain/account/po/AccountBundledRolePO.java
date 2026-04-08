package com.ranyk.vt.boot.example.web.freamwork.domain.account.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: AccountBundledRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的账户绑定角色信息 PO 类,字段说明:<br/>
 * <ul>
 *     <li>accountIds: 账户ID 列表</li>
 *     <li>roleIds: 角色ID 列表</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record AccountBundledRolePO(List<String> accountIds, List<String> roleIds) implements Serializable {
}
