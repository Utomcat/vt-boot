package com.ranyk.vt.boot.example.web.freamwork.domain.account.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除账户信息参数封装对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 账户信息ID</li>
 *     <li>ids: 账户信息ID List 集合</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record DeleteAccountPO(String id, List<String> ids) implements Serializable {
}
