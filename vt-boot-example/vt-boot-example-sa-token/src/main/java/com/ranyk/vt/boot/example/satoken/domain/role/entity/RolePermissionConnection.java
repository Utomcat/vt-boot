package com.ranyk.vt.boot.example.satoken.domain.role.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: RolePermissionConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库角色权限关联关系表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
@TableName("role_permission_connection")
public class RolePermissionConnection extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1744549730431796778L;

    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 权限ID
     */
    private String permissionId;
}
