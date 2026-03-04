package com.ranyk.vt.boot.example.satoken.mapper.role;

import com.ranyk.vt.boot.example.satoken.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.entity.Role;
import com.ranyk.vt.boot.example.satoken.domain.role.entity.RolePermissionConnection;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CLASS_NAME: RoleMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色信息数据转换接口
 * @date: 2026-03-03
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    /**
     * 数据实体 List 集合转换成 数据传输对象 List 集合
     *
     * @param roles 角色信息数据实体对象 {@link Role} List 集合
     * @return 角色信息数据传输对象 {@link RoleDTO} List 集合
     */
    List<RoleDTO> roleEntityListToRoleDTOList(List<Role> roles);

    /**
     * 数据实体 List 集合转换成 数据传输对象 List 集合
     *
     * @param rolePermissionConnections 角色权限关联关系数据实体对象 {@link RolePermissionConnection} List 集合
     * @return 角色权限关联关系数据传输对象 {@link RolePermissionConnectionDTO} List 集合
     */
    List<RolePermissionConnectionDTO> rolePermissionConnectionEntityListToRolePermissionConnectionDTOList(List<RolePermissionConnection> rolePermissionConnections);
}
