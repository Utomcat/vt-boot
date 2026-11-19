package com.ranyk.vt.boot.example.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.base.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysRole.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色实体, 对应表 t_role
 * @date: 2026-11-20
 */
@Data
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_role")
public class SysRole extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6172348891533077462L;

    /**
     * 角色编码, 如 ADMIN / USER, 租户内唯一
     */
    private String code;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 是否内置角色: 1-是(不可删除、不可停用) 0-否
     */
    private Integer builtin;
    /**
     * 排序值, 越小越靠前
     */
    private Integer sort;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
}
