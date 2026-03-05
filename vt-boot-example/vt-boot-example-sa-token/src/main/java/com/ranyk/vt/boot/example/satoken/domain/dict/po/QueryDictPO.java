package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: QueryDictPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的查询字典值参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QueryDictPO extends BasePO {
    @Serial
    private static final long serialVersionUID = -3261620769782933407L;

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
}
