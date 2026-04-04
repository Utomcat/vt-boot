package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteUserPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除用户信息数据封装 PO 类, 字段说明:<br/>
 * <ul>
 *     <li>id: 需要删除的用户ID, 单条数据删除时,该字段不能为空</li>
 *     <li>ids: 需要删除的用户ID列表, 批量删除时,该字段不能为空, 且不能同时与 id 字段指定</li>
 * </ul>
 * @date: 2026-03-19
 */
@Builder
public record DeleteUserPO(String id, List<String> ids) implements Serializable {
}
