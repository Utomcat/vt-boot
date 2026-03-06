package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QueryDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的查询字典类型参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class QueryDictTypePO implements Serializable {
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
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    private Integer status;
    /**
     * 备注, 默认值为空字符串
     */
    private String remark;
}
