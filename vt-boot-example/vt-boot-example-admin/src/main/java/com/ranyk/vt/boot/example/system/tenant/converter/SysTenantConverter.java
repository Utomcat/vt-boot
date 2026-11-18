package com.ranyk.vt.boot.example.system.tenant.converter;

import com.ranyk.vt.boot.example.system.tenant.domain.dto.TenantFormDTO;
import com.ranyk.vt.boot.example.system.tenant.domain.dto.TenantQueryDTO;
import com.ranyk.vt.boot.example.system.tenant.domain.entity.SysTenant;
import com.ranyk.vt.boot.example.system.tenant.domain.po.TenantFormPO;
import com.ranyk.vt.boot.example.system.tenant.domain.po.TenantQueryPO;
import com.ranyk.vt.boot.example.system.tenant.domain.vo.TenantVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: SysTenantConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户实体与 DTO/VO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-16
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysTenantConverter {

    /**
     * 接口入参转业务查询 DTO(API 层入参 -> Service 层参数)
     *
     * @param queryPO 租户分页查询接口入参
     * @return 租户分页查询参数 {@link TenantQueryDTO}
     */
    TenantQueryDTO toQueryDTO(TenantQueryPO queryPO);

    /**
     * 接口入参转业务表单 DTO(API 层入参 -> Service 层参数)
     *
     * @param formPO 租户新增/修改接口入参
     * @return 租户表单参数 {@link TenantFormDTO}
     */
    TenantFormDTO toFormDTO(TenantFormPO formPO);

    /**
     * 租户实体转租户视图对象
     *
     * @param tenant 租户实体
     * @return 租户视图对象 {@link TenantVO}
     */
    TenantVO toVO(SysTenant tenant);

    /**
     * 租户表单转租户实体(新增场景), 主键与审计字段由数据库自动填充
     *
     * @param formDTO 租户表单参数
     * @return 租户实体 {@link SysTenant}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SysTenant toEntity(TenantFormDTO formDTO);

    /**
     * 将租户表单的非空字段合并到已有租户实体(修改场景); 租户编码与审计字段不在此处覆盖
     *
     * @param formDTO 租户表单参数
     * @param tenant  待更新的租户实体
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
    void updateEntity(TenantFormDTO formDTO, @MappingTarget SysTenant tenant);
}
