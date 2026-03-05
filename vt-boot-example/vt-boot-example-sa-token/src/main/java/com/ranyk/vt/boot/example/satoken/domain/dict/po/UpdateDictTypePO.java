package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: UpdateDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的修改字典类型参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdateDictTypePO extends BasePO {
    @Serial
    private static final long serialVersionUID = -2861496231675803487L;

    /**
     * 字典类型名称
     */
    private String name;
    /**
     * 字典类型编码
     */
    private String code;
}
