package com.ranyk.vt.boot.example.system.role.converter;

import com.ranyk.vt.boot.example.system.role.domain.dto.RoleFormDTO;
import com.ranyk.vt.boot.example.system.role.domain.dto.RoleQueryDTO;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRole;
import com.ranyk.vt.boot.example.system.role.domain.po.RoleFormPO;
import com.ranyk.vt.boot.example.system.role.domain.po.RoleQueryPO;
import com.ranyk.vt.boot.example.system.role.domain.vo.RoleVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: SysRoleConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色实体与 DTO/VO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-20
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysRoleConverter {

    /**
     * 接口入参转业务查询 DTO
     *
     * @param queryPO 角色分页查询接口入参
     * @return 角色分页查询参数 {@link RoleQueryDTO}
     */
    RoleQueryDTO toQueryDTO(RoleQueryPO queryPO);

    /**
     * 接口入参转业务表单 DTO
     *
     * @param formPO 角色新增/修改接口入参
     * @return 角色表单参数 {@link RoleFormDTO}
     */
    RoleFormDTO toFormDTO(RoleFormPO formPO);

    /**
     * 角色实体转角色视图对象, 授权的菜单ID由业务层单独填充
     *
     * @param role 角色实体
     * @return 角色视图对象 {@link RoleVO}
     */
    @Mapping(target = "menuIds", ignore = true)
    RoleVO toVO(SysRole role);

    /**
     * 角色表单转角色实体(新增场景), 主键、内置标记与审计字段由业务层或数据库填充
     *
     * @param formDTO 角色表单参数
     * @return 角色实体 {@link SysRole}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "builtin", ignore = true)
    SysRole toEntity(RoleFormDTO formDTO);

    /**
     * 将角色表单的非空字段合并到已有角色实体(修改场景); 角色编码与审计字段不在此处覆盖
     *
     * @param formDTO 角色表单参数
     * @param role    待更新的角色实体
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "builtin", ignore = true)
    void updateEntity(RoleFormDTO formDTO, @MappingTarget SysRole role);
}
