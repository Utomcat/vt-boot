package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: QueryDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的查询字典类型参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QueryDictTypePO  extends BasePO {
    @Serial
    private static final long serialVersionUID = 5466790545081056582L;

    /**
     * 字典类型名
     */
    private String name;
    /**
     * 字典类型编码
     */
    private String code;
}
