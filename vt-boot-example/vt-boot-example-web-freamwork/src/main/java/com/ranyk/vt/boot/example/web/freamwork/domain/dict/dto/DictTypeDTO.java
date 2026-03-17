package com.ranyk.vt.boot.example.web.freamwork.domain.dict.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysDictTypeDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库字典类型表数据传输对象类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DictTypeDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -7417874313606432460L;
    /**
     * 字典类型名
     */
    private String name;
    /**
     * 字典类型编码
     */
    private String code;

    // 以下为扩展字段

}
