package com.ranyk.vt.boot.example.system.dict.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: DictTypeFormDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型新增/修改参数
 * @date: 2026-11-20
 */
@Data
public class DictTypeFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1178664512360384325L;

    /**
     * 字典类型ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 字典类型编码
     */
    private String dictType;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}
