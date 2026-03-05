package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: UpdateDictPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的修改字典值参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateDictPO extends BasePO {
    @Serial
    private static final long serialVersionUID = -6171572507886267555L;

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
}
