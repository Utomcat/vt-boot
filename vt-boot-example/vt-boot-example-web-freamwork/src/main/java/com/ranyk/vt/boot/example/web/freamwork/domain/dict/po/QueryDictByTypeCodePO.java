package com.ranyk.vt.boot.example.web.freamwork.domain.dict.po;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryDictByTypeCodePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的通过字典类型 code 查询对应的字典数据 PO 参数对象, 字段说明:<br/>
 * <ul>
 *     <li>code: 字典类型 code</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record QueryDictByTypeCodePO(String code) implements Serializable {
}
