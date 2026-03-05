package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: SaveDictPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的新增字典值参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class SaveDictPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3263912862878561664L;

    /**
     * 字典类型ID
     */
    private String dictTypeId;
    /**
     * 字典值名称
     */
    private String name;
    /**
     * 字典值编码
     */
    private String code;
    /**
     * 字典值
     */
    private String value;
    /**
     * 字典类型备注
     */
    private String remark;
}
