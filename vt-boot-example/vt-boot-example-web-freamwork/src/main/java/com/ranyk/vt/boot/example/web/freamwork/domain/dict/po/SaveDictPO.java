package com.ranyk.vt.boot.example.web.freamwork.domain.dict.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: SaveDictPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的新增字典值参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>dictTypeId: 字典类型ID, 不能为空</li>
 *     <li>name: 字典值名称</li>
 *     <li>code: 字典值编码</li>
 *     <li>value: 字典值</li>
 *     <li>remark: 描述, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record SaveDictPO(@Nonnull String dictTypeId, @Nonnull String name, @Nonnull String code, @Nonnull String value, String remark) implements Serializable {

}
