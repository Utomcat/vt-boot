package com.ranyk.vt.boot.example.system.dict.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: DictTypeFormPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型新增/修改接口入参
 * @date: 2026-11-20
 */
@Data
public class DictTypeFormPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6534981342568653162L;

    /**
     * 字典类型ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 字典名称, 必填
     */
    private String dictName;
    /**
     * 字典类型编码, 必填且唯一, 如 sys_user_status
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
