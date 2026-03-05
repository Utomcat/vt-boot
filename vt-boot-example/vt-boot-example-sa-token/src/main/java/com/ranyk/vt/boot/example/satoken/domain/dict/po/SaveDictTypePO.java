package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: SaveDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的新增字典类型参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SaveDictTypePO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6113004209606441359L;

    /**
     * 字典类型名称
     */
    private String name;
    /**
     * 字典类型编码
     */
    private String code;
    /**
     * 字典类型备注
     */
    private String remark;
}
