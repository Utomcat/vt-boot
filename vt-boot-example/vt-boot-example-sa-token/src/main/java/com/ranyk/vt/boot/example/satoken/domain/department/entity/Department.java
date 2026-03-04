package com.ranyk.vt.boot.example.satoken.domain.department.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: Department.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库部门信息表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@TableName("department_info")
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -553289623777926129L;

    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门编码
     */
    private String code;
    /**
     * 父级部门ID
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 父级部门ID列表, 逗号分隔
     */
    @TableField("parent_ids")
    private String parentIds;
}
