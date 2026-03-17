package com.ranyk.vt.boot.example.web.freamwork.domain.dict.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的删除字典类型参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 字典类型ID</li>
 *     <li>ids: 字典类型ID列表</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record DeleteDictTypePO(String id, List<String> ids) implements Serializable {
}
