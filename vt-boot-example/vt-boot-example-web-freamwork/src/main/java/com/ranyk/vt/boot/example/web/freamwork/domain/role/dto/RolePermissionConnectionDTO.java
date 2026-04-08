package com.ranyk.vt.boot.example.web.freamwork.domain.role.dto;

import cn.hutool.core.collection.CollUtil;
import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * 构建角色权限关系数据传输对象列表
     *
     * @return 角色权限关系数据传输对象列表, {@link RolePermissionConnectionDTO}
     */
    public List<RolePermissionConnectionDTO> buildRolePermissionConnectionDTOList() {
        if (CollUtil.isEmpty(this.roleIds) || CollUtil.isEmpty(this.permissionIds)) {
            return Collections.emptyList();
        }
        List<RolePermissionConnectionDTO> rolePermissionConnectionDTOList = new ArrayList<>(Math.max(this.roleIds.size(), this.permissionIds.size()));
        this.roleIds.forEach(roleId -> this.permissionIds.forEach(permissionId -> rolePermissionConnectionDTOList.add(RolePermissionConnectionDTO.builder().roleId(roleId).permissionId(permissionId).build())));
        return rolePermissionConnectionDTOList;
    }
}
