package com.ranyk.vt.boot.example.web.freamwork.domain.dict.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: SaveDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的新增字典类型参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 字典类型名称</li>
 *     <li>code: 字典类型编码</li>
 *     <li>remark: 描述, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-05
 */
@Builder
public record SaveDictTypePO(@Nonnull String name, @Nonnull String code, String remark) implements Serializable {

}
