package com.ranyk.vt.boot.example.satoken.mapper.department;

import com.ranyk.vt.boot.example.satoken.domain.department.dto.DepartmentDTO;
import com.ranyk.vt.boot.example.satoken.domain.department.entity.Department;
import com.ranyk.vt.boot.example.satoken.domain.department.po.DeleteDepartmentPO;
import com.ranyk.vt.boot.example.satoken.domain.department.po.QueryDepartmentPO;
import com.ranyk.vt.boot.example.satoken.domain.department.po.SaveDepartmentPO;
import com.ranyk.vt.boot.example.satoken.domain.department.po.UpdateDepartmentPO;
import com.ranyk.vt.boot.example.satoken.domain.department.vo.QueryDepartmentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * CLASS_NAME: DepartmentMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门信息数据转换对象
 * @date: 2026-03-03
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    /**
     * 保存部门信息参数对象 PO 转换为 DTO
     *
     * @param saveDepartmentPO 保存部门信息参数对象 PO {@link SaveDepartmentPO}
     * @return 部门信息数据传输对象 DTO {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "parentId", target = "parentId"),
            @Mapping(source = "parentIds", target = "parentIds"),
            @Mapping(source = "remark", target = "remark"),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    DepartmentDTO saveDepartmentPOToDTO(SaveDepartmentPO saveDepartmentPO);

    /**
     * 删除部门信息参数对象 PO 转换为 DTO
     *
     * @param deleteDepartmentPO 删除部门信息参数对象 PO {@link DeleteDepartmentPO}
     * @return 部门信息数据传输对象 DTO {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "ids", target = "ids"),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "parentId", ignore = true),
            @Mapping(target = "parentIds", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true)
    })
    DepartmentDTO deleteDepartmentPOToDTO(DeleteDepartmentPO deleteDepartmentPO);

    /**
     * 修改部门信息参数对象 PO 转换为 DTO
     *
     * @param updateDepartmentPO 修改部门信息参数对象 PO {@link UpdateDepartmentPO}
     * @return 部门信息数据传输对象 DTO {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "parentId", target = "parentId"),
            @Mapping(source = "parentIds", target = "parentIds"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "remark", target = "remark"),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    DepartmentDTO updateDepartmentPOToDTO(UpdateDepartmentPO updateDepartmentPO);

    /**
     * 查询部门信息参数对象 PO 转换为 DTO
     *
     * @param queryDepartmentPO 查询部门信息参数对象 PO {@link QueryDepartmentPO}
     * @return 部门信息数据传输对象 DTO {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "remark", target = "remark"),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "parentId", ignore = true),
            @Mapping(target = "parentIds", ignore = true)
    })
    DepartmentDTO queryDepartmentPOToDTO(QueryDepartmentPO queryDepartmentPO);

    /**
     * 部门信息数据传输对象 DTO 转换为实体对象
     *
     * @param departmentDTO 部门信息数据传输对象 DTO {@link DepartmentDTO}
     * @return 部门信息实体对象 {@link Department}
     */
    Department dtoToEntity(DepartmentDTO departmentDTO);

    /**
     * 部门信息实体对象转换为数据传输对象
     *
     * @param department 部门信息实体对象 {@link Department}
     * @return 部门信息数据传输对象 DTO {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage",ignore = true),
            @Mapping(target = "pageSize",ignore = true),
            @Mapping(target = "ids",ignore = true)
    })
    DepartmentDTO departmentToDepartmentDTO(Department department);

    /**
     * 部门信息实体对象转换为数据传输对象
     *
     * @param departments 部门信息实体对象List 集合 - {@link Department}
     * @return 部门信息数据传输对象 DTO {@link DepartmentDTO}
     */
    List<DepartmentDTO> entityListToDTOList(List<Department> departments);

    /**
     * 部门信息数据传输对象 DTO 列表转换为查询部门信息数据传输对象 VO 列表
     *
     * @param departmentDTOs 部门信息数据传输对象 DTO 列表 - {@link DepartmentDTO}
     * @return 查询部门信息数据传输对象 VO 列表 - {@link QueryDepartmentVO}
     */
    List<QueryDepartmentVO> dtoListToQueryDepartmentVOList(List<DepartmentDTO> departmentDTOs);

}
