package com.ranyk.vt.boot.example.satoken.mapper.role;

import com.ranyk.vt.boot.example.satoken.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.role.entity.Role;
import com.ranyk.vt.boot.example.satoken.domain.role.entity.RolePermissionConnection;
import com.ranyk.vt.boot.example.satoken.domain.role.po.DeleteRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.po.QueryRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.po.SaveRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.po.UpdateRolePO;
import com.ranyk.vt.boot.example.satoken.domain.role.vo.QueryRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
     * 将 新增角色请求参数封装 PO 类 转换为 角色数据传输对象
     *
     * @param saveRolePO 新增角色请求参数封装 PO 类 {@link SaveRolePO}
     * @return 角色数据传输对象 {@link RoleDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    RoleDTO saveRolePOToRoleDTO(SaveRolePO saveRolePO);

    /**
     * 将 删除角色请求参数封装 PO 类 转换为 角色数据传输对象
     *
     * @param deleteRolePO 删除角色请求参数封装 PO 类 {@link DeleteRolePO}
     * @return 角色数据传输对象 {@link RoleDTO}
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
            @Mapping(target = "remark", ignore = true),
    })
    RoleDTO deleteRolePOToRoleDTO(DeleteRolePO deleteRolePO);

    /**
     * 将 修改角色请求参数封装 PO 类 转换为 角色数据传输对象
     *
     * @param updateRolePO 修改角色请求参数封装 PO 类 {@link UpdateRolePO}
     * @return 角色数据传输对象 {@link RoleDTO}
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
    RoleDTO updateRolePOToRoleDTO(UpdateRolePO updateRolePO);

    /**
     * 将 角色查询请求参数封装 PO 类 转换为 角色数据传输对象
     *
     * @param queryRolePO 角色查询请求参数封装 PO 类 {@link QueryRolePO}
     * @return 角色数据传输对象 {@link RoleDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    RoleDTO queryRolePOToRoleDTO(QueryRolePO queryRolePO);

    /**
     * 将 角色数据传输对象 转换为 角色信息数据实体对象
     *
     * @param roleDTO 角色数据传输对象 {@link RoleDTO}
     * @return 角色信息数据实体对象 {@link Role}
     */
    Role dtoToEntity(RoleDTO roleDTO);

    /**
     * 数据实体 List 集合转换成 数据传输对象 List 集合
     *
     * @param roles 角色信息数据实体对象 {@link Role} List 集合
     * @return 角色信息数据传输对象 {@link RoleDTO} List 集合
     */
    List<RoleDTO> roleEntityListToRoleDTOList(List<Role> roles);

    /**
     * 数据传输对象 List 集合 转换成 角色查询视图对象 List 集合
     *
     * @param roleDTOS 角色信息数据传输对象 {@link RoleDTO} List 集合
     * @return 角色信息数据传输对象 {@link QueryRoleVO} List 集合
     */
    List<QueryRoleVO> roleDTOListToQueryRoleVOList(List<RoleDTO> roleDTOS);

    /**
     * 数据实体 List 集合转换成 数据传输对象 List 集合
     *
     * @param rolePermissionConnections 角色权限关联关系数据实体对象 {@link RolePermissionConnection} List 集合
     * @return 角色权限关联关系数据传输对象 {@link RolePermissionConnectionDTO} List 集合
     */
    List<RolePermissionConnectionDTO> rolePermissionConnectionEntityListToRolePermissionConnectionDTOList(List<RolePermissionConnection> rolePermissionConnections);
}
