package com.ranyk.vt.boot.example.satoken.mapper.permissions;

import com.ranyk.vt.boot.example.satoken.domain.permissions.dto.PermissionDTO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.entity.Permission;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.DeletePermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.QueryPermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.SavePermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.po.UpdatePermissionPO;
import com.ranyk.vt.boot.example.satoken.domain.permissions.vo.QueryPermissionVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
     * 将 新增权限请求参数封装 PO 类 转换为 权限数据传输对象
     *
     * @param savePermissionPO 新增权限请求参数封装 PO 类 {@link SavePermissionPO}
     * @return 权限数据传输对象 {@link PermissionDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    PermissionDTO savePermissionPOToPermissionDTO(SavePermissionPO savePermissionPO);

    /**
     * 将 删除权限请求参数封装 PO 类 转换为 权限数据传输对象
     *
     * @param deletePermissionPO 删除权限请求参数封装 PO 类 {@link DeletePermissionPO}
     * @return 权限数据传输对象 {@link PermissionDTO}
     */
    @Mappings({
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "remark", ignore = true),
    })
    PermissionDTO deletePermissionPOToPermissionDTO(DeletePermissionPO deletePermissionPO);

    /**
     * 将 修改权限请求参数封装 PO 类 转换为 权限数据传输对象
     *
     * @param updatePermissionPO 修改权限请求参数封装 PO 类 {@link UpdatePermissionPO}
     * @return 权限数据传输对象 {@link PermissionDTO}
     */
    @Mappings({
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    PermissionDTO updatePermissionPOToPermissionDTO(UpdatePermissionPO updatePermissionPO);

    /**
     * 将 查询权限请求参数封装 PO 类 转换为 权限数据传输对象
     *
     * @param queryPermissionPO 查询权限请求参数封装 PO 类 {@link QueryPermissionPO}
     * @return 权限数据传输对象 {@link PermissionDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    PermissionDTO queryPermissionPoToPermissionDTO(QueryPermissionPO queryPermissionPO);

    /**
     *
     * 将 权限数据传输对象 转换为 权限实体对象
     *
     * @param permissionDTO 权限数据传输对象 {@link PermissionDTO}
     * @return 权限实体对象 {@link Permission}
     */
    Permission dtoToEntity(PermissionDTO permissionDTO);

    /**
     *
     * 将 权限实体对象 转换为 角色查询权限数据传输对象
     *
     * @param permission 角色查询权限数据传输对象 {@link Permission}
     * @return 角色查询权限数据传输对象 {@link QueryPermissionVO}
     */
    @Mappings({
            @Mapping(target = "currentPage",ignore = true),
            @Mapping(target = "pageSize",ignore = true),
            @Mapping(target = "ids",ignore = true)
    })
    PermissionDTO permissionToPermissionDTO(Permission permission);

    /**
     *
     * 将 权限实体对象 List 集合 转换为 权限数据传输对象 List 集合
     *
     * @param permissions 权限实体对象 {@link Permission} List 集合
     * @return 权限数据传输对象 {@link PermissionDTO} List 集合
     */
    List<PermissionDTO> permissionEntityListToPermissionDTOList(List<Permission> permissions);

    /**
     * 将 权限数据传输对象 List 集合 转换为 角色查询权限数据传输对象 List 集合
     *
     * @param permissionDTOS 角色查询权限数据传输对象 {@link PermissionDTO} List 集合
     * @return 角色查询权限数据传输对象 {@link QueryPermissionVO} List 集合
     */
    List<QueryPermissionVO> permissionDTOListToQueryPermissionVOList(List<PermissionDTO> permissionDTOS);
}
