package com.ranyk.vt.boot.example.web.freamwork.domain.dict.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysDictDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典表数据传输对象类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class DictDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -9038803797829011709L;
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

    // 以下为扩展字段

}
