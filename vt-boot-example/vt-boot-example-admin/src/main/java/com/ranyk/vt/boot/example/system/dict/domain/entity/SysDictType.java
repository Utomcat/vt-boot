package com.ranyk.vt.boot.example.system.dict.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.base.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysDictType.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型实体, 对应表 t_dict_type
 * @date: 2026-11-20
 */
@Data
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_dict_type")
public class SysDictType extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 3219786415563194772L;

    /**
     * 字典名称, 便于阅读的中文名称, 如 用户状态
     */
    private String dictName;
    /**
     * 字典类型编码, 业务引用的唯一标识, 如 sys_user_status
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
}
