package com.ranyk.vt.boot.example.system.dict.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: DictTypeVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型返回结果
 * @date: 2026-11-20
 */
@Data
public class DictTypeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4506173824761390743L;

    /**
     * 字典类型ID
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
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
