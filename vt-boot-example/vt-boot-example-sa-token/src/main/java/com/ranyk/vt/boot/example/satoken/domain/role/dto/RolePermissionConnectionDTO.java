package com.ranyk.vt.boot.example.satoken.domain.role.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * CLASS_NAME: RolePermissionConnectionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色权限关联关系数据传输对象
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RolePermissionConnectionDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -4607844330211574757L;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 权限ID
     */
    private String permissionId;

    // 以下为扩展属性字段

    /**
     * 角色ID集合
     */
    private List<String> roleIds;
    /**
     * 权限ID集合
     */
    private List<String> permissionIds;
}
