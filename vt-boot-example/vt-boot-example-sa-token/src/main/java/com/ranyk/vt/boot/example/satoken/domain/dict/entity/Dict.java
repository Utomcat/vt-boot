package com.ranyk.vt.boot.example.satoken.domain.dict.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysDict.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库字典表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dict")
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class Dict extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -8442223943674617920L;
    /**
     * 字典类型ID
     */
    @TableField("dict_type_id")
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
