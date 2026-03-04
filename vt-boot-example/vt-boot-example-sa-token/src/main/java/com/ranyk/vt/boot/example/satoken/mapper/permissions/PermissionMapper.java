package com.ranyk.vt.boot.example.satoken.mapper.permissions;

import com.ranyk.vt.boot.example.satoken.domain.permissions.dto.PermissionDTO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.entity.Permission;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CLASS_NAME: PermissionMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限数据转换接口类
 * @date: 2026-03-03
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {

    /**
     *
     * 将 权限实体对象 List 集合 转换为 权限数据传输对象 List 集合
     *
     * @param permissions 权限实体对象 {@link Permission} List 集合
     * @return 权限数据传输对象 {@link PermissionDTO} List 集合
     */
    List<PermissionDTO> permissionEntityToPermissionDTO(List<Permission> permissions);
}
