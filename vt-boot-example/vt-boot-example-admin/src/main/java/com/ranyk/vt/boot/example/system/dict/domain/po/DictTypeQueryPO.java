package com.ranyk.vt.boot.example.system.dict.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: DictTypeQueryPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型分页查询接口入参
 * @date: 2026-11-20
 */
@Data
public class DictTypeQueryPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1470249382713492847L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 字典名称, 模糊匹配
     */
    private String dictName;
    /**
     * 字典类型编码, 模糊匹配
     */
    private String dictType;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
}
