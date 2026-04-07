package com.ranyk.vt.boot.example.web.freamwork.mapper.tenant;

import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.dto.TenantDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.entity.Tenant;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.DeleteTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.QueryTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.SaveTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.po.UpdateTenantPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.vo.QueryTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * CLASS_NAME: TenantMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户信息数据转换接口
 * @date: 2026-04-06
 */
@Mapper(componentModel = "spring")
public interface TenantMapper {

    /**
     * 保存租户信息数据封装 PO 对象转换成租户信息数据传输 DTO 对象
     *
     * @param saveTenantPO 保存租户信息数据封装 PO 对象 {@link SaveTenantPO}
     * @return 租户信息数据传输 DTO 对象 {@link TenantDTO}
     */
    @Mappings({
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
    TenantDTO saveTenantPOToTenantDTO(SaveTenantPO saveTenantPO);

    /**
     * 删除租户信息数据封装 PO 对象转换成租户信息数据传输 DTO 对象
     *
     * @param deleteTenantPO 删除租户信息数据封装 PO 对象 {@link DeleteTenantPO}
     * @return 租户信息数据传输 DTO 对象 {@link TenantDTO}
     */
    @Mappings({
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
    TenantDTO deleteTenantPOToTenantDTO(DeleteTenantPO deleteTenantPO);

    /**
     * 更新租户信息数据封装 PO 对象转换成租户信息数据传输 DTO 对象
     *
     * @param updateTenantPO 更新租户信息数据封装 PO 对象 {@link UpdateTenantPO}
     * @return 租户信息数据传输 DTO 对象 {@link TenantDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    TenantDTO updateTenantPOToTenantDTO(UpdateTenantPO updateTenantPO);

    /**
     * 查询租户信息数据封装 PO 对象转换成租户信息数据传输 DTO 对象
     *
     * @param queryTenantPO 查询租户信息数据封装 PO 对象 {@link QueryTenantPO}
     * @return 租户信息数据传输 DTO 对象 {@link TenantDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    TenantDTO queryTenantPOToTenantDTO(QueryTenantPO queryTenantPO);

    /**
     * 租户信息数据传输 DTO 对象转换为租户信息数据实体对象
     *
     * @param tenantDTO 租户信息数据传输 DTO 集合 {@link TenantDTO}
     * @return 租户信息数据实体对象 {@link Tenant}
     */
    Tenant tenantDTOToTenant(TenantDTO tenantDTO);

    /**
     * 租户信息数据实体对象转换为租户信息数据传输 DTO 对象
     *
     * @param tenant 租户信息数据实体对象 {@link Tenant}
     * @return 租户信息数据传输 DTO 对象 {@link TenantDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    TenantDTO tenantToTenantDTO(Tenant tenant);

    /**
     * 租户信息数据实体对象列表转换为租户信息数据传输 DTO 对象列表
     *
     * @param tenantList 租户信息数据实体对象列表 {@link Tenant}
     * @return 租户信息数据传输 DTO 对象列表 {@link TenantDTO}
     */
    List<TenantDTO> tenantListToTenantDTOList(List<Tenant> tenantList);

    /**
     * 租户信息数据传输 DTO 对象列表转换为查询租户信息数据视图 VO 对象列表
     *
     * @param tenantDTOList 租户信息数据传输 DTO 集合 {@link TenantDTO}
     * @return 查询租户信息数据视图 VO 集合 {@link QueryTenantVO}
     */
    List<QueryTenantVO> tenantDTOListToQueryTenantVOList(List<TenantDTO> tenantDTOList);
}