package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UpdateDictPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的修改字典值参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateDictPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6171572507886267555L;

    /**
     * 字典值ID
     */
    private String id;
    /**
     * 字典类型ID
     */
    private String dictTypeId;
    /**
     * 字典值名称
     */
    private String name;
    /**
     * 字典值编码
     */
    private String code;
    /**
     * 字典值
     */
    private String value;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    @Builder.Default
    private Integer status = 1;
    /**
     * 字典类型备注
     */
    private String remark;
}
