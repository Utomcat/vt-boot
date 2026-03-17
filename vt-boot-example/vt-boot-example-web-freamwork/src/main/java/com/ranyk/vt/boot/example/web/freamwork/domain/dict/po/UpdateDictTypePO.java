package com.ranyk.vt.boot.example.web.freamwork.domain.dict.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: UpdateDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的修改字典类型参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 字典类型ID, 不能为空</li>
 *     <li>name: 字典类型名称</li>
 *     <li>code: 字典类型编码</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态(默认值);）</li>
 *     <li>remark: 描述, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record UpdateDictTypePO(@Nonnull String id, String name, String code, Integer status, String remark) implements Serializable {
    public UpdateDictTypePO {
        if (Objects.isNull(status)) {
            status = 1;
        }
    }
}

