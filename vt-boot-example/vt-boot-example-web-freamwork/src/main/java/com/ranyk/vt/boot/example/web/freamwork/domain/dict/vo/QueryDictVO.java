package com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryDictVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询指定字典类型的字典数据返回前端视图 VO , 字段说明: <br/>
 * <ul>
 *     <li>code: 字典编码</li>
 *     <li>name: 字典名称</li>
 *     <li>value: 字典值</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record QueryDictVO(String code, String name, String value) implements Serializable {
}
