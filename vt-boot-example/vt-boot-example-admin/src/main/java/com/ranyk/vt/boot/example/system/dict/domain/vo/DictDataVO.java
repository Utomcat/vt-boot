package com.ranyk.vt.boot.example.system.dict.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: DictDataVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据返回结果
 * @date: 2026-11-20
 */
@Data
public class DictDataVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2904618239471823057L;

    /**
     * 字典数据ID
     */
    private Long id;
    /**
     * 所属字典类型编码
     */
    private String dictType;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 字典键值
     */
    private String dictValue;
    /**
     * 排序值
     */
    private Integer dictSort;
    /**
     * 标签样式
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
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
