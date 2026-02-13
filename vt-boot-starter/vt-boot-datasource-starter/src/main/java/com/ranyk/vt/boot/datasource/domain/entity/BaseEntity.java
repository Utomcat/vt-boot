package com.ranyk.vt.boot.datasource.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: BaseEntity.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库实体基础类
 * @date: 2026-02-13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper=true)
@SuppressWarnings("unused")
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5642605741784041103L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT, value = "1")
    private Integer status;
    /**
     * 备注, 默认值为空字符串
     */
    @TableField(fill = FieldFill.INSERT)
    private String remark;
    /**
     * 创建时间, 默认值为当前时间
     */
    @TableField(fill = FieldFill.INSERT, value = "now()")
    private LocalDateTime createTime;
    /**
     * 创建人 ID, 默认值为 1
     */
    @TableField(fill = FieldFill.INSERT, value = "1")
    private Long  createBy;
    /**
     * 更新时间, 默认值为当前时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "now()")
    private LocalDateTime updateTime;
    /**
     * 更新人 ID, 默认值为 1
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "1")
    private Long updateBy;
}
