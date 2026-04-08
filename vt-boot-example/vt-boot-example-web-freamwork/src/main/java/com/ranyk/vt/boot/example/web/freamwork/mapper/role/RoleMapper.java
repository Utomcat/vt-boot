package com.ranyk.vt.boot.example.web.freamwork.mapper.role;

import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.dto.RoleDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.dto.RolePermissionConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.entity.Role;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.entity.RolePermissionConnection;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.po.*;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.vo.QueryAccountRoleVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.vo.QueryRoleVO;
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
     * 保存角色信息数据封装 PO 对象 转换成 角色信息数据传输 DTO 对象
     *
     * @param saveRolePO 保存角色信息数据封装 PO 对象 {@link SaveRolePO}
     * @return 角色信息数据传输 DTO 对象 {@link RoleDTO}
     */
    @Mappings({
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    RoleDTO saveRolePOToRoleDTO(SaveRolePO saveRolePO);

    /**
     * 删除角色信息数据封装 PO 对象 转换成 角色信息数据传输 DTO 对象
     *
     * @param deleteRolePO 删除角色信息数据封装 PO 对象 {@link DeleteRolePO}
     * @return 角色信息数据传输 DTO 对象 {@link RoleDTO}
     */
    @Mappings({
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
    })
    RoleDTO deleteRolePOToRoleDTO(DeleteRolePO deleteRolePO);

    /**
     * 更新角色信息数据封装 PO 对象 转换成 角色信息数据传输 DTO 对象
     *
     * @param updateRolePO 更新角色信息数据封装 PO 对象 {@link UpdateRolePO}
     * @return 角色信息数据传输 DTO 对象 {@link RoleDTO}
     */
    @Mappings({
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    RoleDTO updateRolePOToRoleDTO(UpdateRolePO updateRolePO);

    /**
     * 查询角色信息数据封装 PO 对象 转换成 角色信息数据传输 DTO 对象
     *
     * @param queryRolePO 查询角色信息数据封装 PO 对象 {@link QueryRolePO}
     * @return 角色信息数据传输 DTO 对象 {@link RoleDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    RoleDTO queryRolePOToRoleDTO(QueryRolePO queryRolePO);

    /**
     * 角色信息数据转换方法 - 数据传输对象 转换为 数据实体对象
     *
     * @param roleDTO 角色信息数据传输对象 {@link RoleDTO}
     * @return 角色信息数据实体对象 {@link Role}
     */
    Role roleDTOToRoleEntity(RoleDTO roleDTO);

    /**
     * 角色信息数据转换方法 - 数据实体对象 转换为 数据传输对象
     *
     * @param role 角色信息数据实体对象 {@link Role}
     * @return 角色信息数据传输对象 {@link RoleDTO}
     */
    @Mappings({
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    RoleDTO roleToRoleDTO(Role role);

    /**
     * 角色信息数据转换方法 - 数据实体对象 转换为 数据传输对象
     *
     * @param roleList 角色信息数据实体对象 {@link Role}
     * @return 角色信息数据传输对象 {@link RoleDTO}
     */
    List<RoleDTO> roleListToRoleDTOList(List<Role> roleList);

    /**
     * 角色信息数据传输 DTO 对象 转换成 查询角色信息数据视图 VO 对象
     *
     * @param roleDTOList 角色信息数据传输 DTO 集合 {@link RoleDTO}
     * @return 查询角色信息数据视图 VO 集合 {@link QueryRoleVO}
     */
    List<QueryRoleVO> roleDTOListToQueryRoleVOList(List<RoleDTO> roleDTOList);

    /**
     * 角色权限关联关系数据实体对象 转换成 角色权限关联关系数据传输对象
     *
     * @param rolePermissionConnection 角色权限关联关系数据实体对象 {@link RolePermissionConnection}
     * @return 角色权限关联关系数据传输对象 {@link RolePermissionConnectionDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "roleIds", ignore = true),
            @Mapping(target = "permissionIds", ignore = true),
    })
    RolePermissionConnectionDTO rolePermissionConnectionToRolePermissionConnectionDTO(RolePermissionConnection rolePermissionConnection);

    /**
     * 角色权限关联关系数据实体对象 列表 转换成 角色权限关联关系数据传输对象 列表
     *
     * @param rolePermissionConnectionList 角色权限关联关系数据实体对象 列表 {@link RolePermissionConnection}
     * @return 角色权限关联关系数据传输对象 列表 {@link RolePermissionConnectionDTO}
     */
    List<RolePermissionConnectionDTO> rolePermissionConnectionListToRolePermissionConnectionDTOList(List<RolePermissionConnection> rolePermissionConnectionList);

    /**
     * 将 角色权限关联关系数据封装 PO 对象 转换成 角色权限关联关系数据传输 DTO 对象
     *
     * @param roleBundledPermissionPO 新增角色权限关联关系数据封装 PO 对象 {@link RoleBundledPermissionPO}
     * @return 角色权限关联关系数据传输 DTO 对象 {@link RolePermissionConnectionDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "roleId", ignore = true),
            @Mapping(target = "permissionId", ignore = true),
            @Mapping(target = "remark",ignore = true),
            @Mapping(target = "status",ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
    })
    RolePermissionConnectionDTO roleBundledPermissionPOToRolePermissionConnectionDTO(RoleBundledPermissionPO roleBundledPermissionPO);

    /**
     * 角色权限关联关系数据转换方法 - 数据传输对象 列表 转换为 数据实体对象 列表
     *
     * @param rolePermissionConnectionDTOS 角色权限关联关系数据传输对象 列表 {@link RolePermissionConnectionDTO}
     * @return 角色权限关联关系数据实体对象 列表 {@link RolePermissionConnection}
     */
    List<RolePermissionConnection> rolePermissionConnectionDTOListToRolePermissionConnectionList(List<RolePermissionConnectionDTO> rolePermissionConnectionDTOS);

    /**
     * 将 通过账户ID查询角色信息 PO 类对象 转换为 账户信息数据传输 DTO 类对象
     *
     * @param queryRoleByAccountIdPO {@link QueryRoleByAccountIdPO}
     * @return 账户信息数据传输 DTO 类对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    RoleDTO queryRoleByAccountIdPOToRoleDTO(QueryRoleByAccountIdPO queryRoleByAccountIdPO);

    /**
     * 将 {@link RoleDTO} 列表 转换为 {@link QueryAccountRoleVO} 列表
     *
     * @param roleDTOList 角色信息传输对象 列表 {@link RoleDTO}
     * @return 查询账户角色信息数据视图 VO 列表 {@link QueryAccountRoleVO}
     */
    List<QueryAccountRoleVO> roleDTOListToQueryAccountRoleVOList(List<RoleDTO> roleDTOList);
}
