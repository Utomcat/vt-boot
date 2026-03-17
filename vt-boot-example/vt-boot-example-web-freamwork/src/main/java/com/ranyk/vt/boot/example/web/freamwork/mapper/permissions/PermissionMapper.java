package com.ranyk.vt.boot.example.web.freamwork.mapper.permissions;

import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.dto.PermissionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.entity.Permission;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po.DeletePermissionPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po.QueryPermissionPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po.SavePermissionPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po.UpdatePermissionPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.vo.QueryPermissionVO;
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
     * 权限保存数据封装 PO 对象 转换成 权限数据传输 DTO 对象
     *
     * @param savePermissionPO 待转换权限保存数据封装 PO 对象, {@link SavePermissionPO}
     * @return 转换后的权限数据传输 DTO 对象, {@link PermissionDTO}
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
    PermissionDTO savePermissionPOToPermissionDTO(SavePermissionPO savePermissionPO);

    /**
     * 删除权限数据封装 PO 对象 转换成 删除权限数据传输 DTO 对象
     *
     * @param deletePermissionPO 待转换权限删除数据封装 PO 对象, {@link DeletePermissionPO}
     * @return 删除权限数据传输 DTO 列表, {@link PermissionDTO}
     */
    @Mappings({
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "type", ignore = true),
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
    PermissionDTO deletePermissionPOToPermissionDTO(DeletePermissionPO deletePermissionPO);

    /**
     * 修改权限数据封装 PO 对象 转换成 修改权限数据传输 DTO 列表
     *
     * @param updatePermissionPO 待转换权限修改数据封装 PO 列表, {@link UpdatePermissionPO}
     * @return 修改权限数据传输 DTO 列表, {@link PermissionDTO}
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
    PermissionDTO updatePermissionPOToPermissionDTO(UpdatePermissionPO updatePermissionPO);

    /**
     * 查询权限数据封装 PO 列表转换成 查询权限数据传输 DTO 列表
     *
     * @param queryPermissionPO 待转换权限查询数据封装 PO 列表, {@link QueryPermissionPO}
     * @return 查询权限数据传输 DTO 列表, {@link PermissionDTO}
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
    PermissionDTO queryPermissionPoToPermissionDTO(QueryPermissionPO queryPermissionPO);

    /**
     * 权限数据传输对象 转换成 权限数据实体对象
     *
     * @param permissionDTO 待转换权限数据传输对象, {@link PermissionDTO}
     * @return 转换后的权限数据实体对象, {@link Permission}
     */
    Permission permissionDTOToPermission(PermissionDTO permissionDTO);

    /**
     * 权限数据实体对象 列表转换成 权限数据传输对象 列表
     *
     * @param permissionList 待转换权限数据实体对象列表, {@link Permission}
     * @return 转换后的权限数据传输对象列表, {@link PermissionDTO}
     */
    List<PermissionDTO> permissionListToPermissionDTOList(List<Permission> permissionList);

    /**
     * 权限数据传输 DTO 对象 列表转换成 权限数据查询结果 VO 视图对象 列表
     *
     * @param permissionDTOList 待转换权限数据传输 DTO 对象列表, {@link PermissionDTO}
     * @return 转换后的权限数据查询结果 VO 视图对象列表, {@link QueryPermissionVO}
     */
    List<QueryPermissionVO> permissionDTOListToQueryPermissionVOList(List<PermissionDTO> permissionDTOList);

}
