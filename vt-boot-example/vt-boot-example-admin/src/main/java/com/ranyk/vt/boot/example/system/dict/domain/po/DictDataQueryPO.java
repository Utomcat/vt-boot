package com.ranyk.vt.boot.example.system.dict.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: DictDataQueryPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据分页查询接口入参
 * @date: 2026-11-20
 */
@Data
public class DictDataQueryPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4612933948287637109L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 所属字典类型编码, 精确匹配, 通常必填
     */
    private String dictType;
    /**
     * 字典标签, 模糊匹配
     */
    private String dictLabel;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
}
