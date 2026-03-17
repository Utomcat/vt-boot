package com.ranyk.vt.boot.example.web.freamwork.domain.dict.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysDictType.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库字典类型表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@TableName("sys_dict_type")
@EqualsAndHashCode(callSuper = true)
public class DictType extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7840887907604091958L;
    /**
     * 字典类型名
     */
    private String name;
    /**
     * 字典类型编码
     */
    private String code;
}