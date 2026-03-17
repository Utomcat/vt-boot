package com.ranyk.vt.boot.example.web.freamwork.domain.role.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: Role.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库角色信息表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("role_info")
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -4411949555180679659L;

    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
}
