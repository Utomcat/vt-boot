package com.ranyk.vt.boot.example.system.dict.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: DictDataQueryDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据分页查询参数
 * @date: 2026-11-20
 */
@Data
public class DictDataQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6394821764763012384L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 所属字典类型编码, 精确匹配
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
