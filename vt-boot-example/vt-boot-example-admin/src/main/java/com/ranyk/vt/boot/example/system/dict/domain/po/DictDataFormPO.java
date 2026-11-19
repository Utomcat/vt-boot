package com.ranyk.vt.boot.example.system.dict.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: DictDataFormPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据新增/修改接口入参
 * @date: 2026-11-20
 */
@Data
public class DictDataFormPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8831974472340175573L;

    /**
     * 字典数据ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 所属字典类型编码, 必填
     */
    private String dictType;
    /**
     * 字典标签, 必填
     */
    private String dictLabel;
    /**
     * 字典键值, 必填
     */
    private String dictValue;
    /**
     * 排序值, 越小越靠前
     */
    private Integer dictSort;
    /**
     * 标签样式: primary / success / warning / danger / info
     */
    private String tagType;
    /**
     * 额外样式类名
     */
    private String cssClass;
    /**
     * 是否默认选中: 1-是 0-否
     */
    private Integer isDefault;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}
