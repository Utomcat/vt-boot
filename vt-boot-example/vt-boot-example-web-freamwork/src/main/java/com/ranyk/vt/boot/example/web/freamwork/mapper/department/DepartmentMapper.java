package com.ranyk.vt.boot.example.web.freamwork.mapper.department;

import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentAccountConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.entity.Department;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.entity.DepartmentAccountConnection;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.DeleteDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.QueryDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.SaveDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.UpdateDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.vo.QueryDepartmentVO;
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
     * 将 保存部门数据 PO 对象 转换为 部门数据传输 DTO 对象
     *
     * @param saveDepartmentPO 部门数据 PO 对象 {@link SaveDepartmentPO}
     * @return 部门数据传输 DTO 对象 {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    DepartmentDTO saveDepartmentPOToDTO(SaveDepartmentPO saveDepartmentPO);

    /**
     * 将 删除部门数据 PO 对象 转换为 部门数据传输 DTO 对象
     *
     * @param deleteDepartmentPO 部门数据 PO 对象 {@link DeleteDepartmentPO}
     * @return 部门数据传输 DTO 对象 {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "parentId", ignore = true),
            @Mapping(target = "parentIds", ignore = true),
            @Mapping(target = "remark", ignore = true),
    })
    DepartmentDTO deleteDepartmentPOToDTO(DeleteDepartmentPO deleteDepartmentPO);

    /**
     * 将 修改部门数据 PO 对象 转换为 部门数据传输 DTO 对象
     *
     * @param updateDepartmentPO 部门数据 PO 对象 {@link UpdateDepartmentPO}
     * @return 部门数据传输 DTO 对象 {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    DepartmentDTO updateDepartmentPOToDTO(UpdateDepartmentPO updateDepartmentPO);

    /**
     * 将 查询部门数据 PO 对象 转换为 部门数据传输 DTO 对象
     *
     * @param queryDepartmentPO 部门数据 PO 对象 {@link QueryDepartmentPO}
     * @return 部门数据传输 DTO 列表 {@link List<DepartmentDTO>}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "parentId", ignore = true),
            @Mapping(target = "parentIds", ignore = true),
    })
    DepartmentDTO queryDepartmentPOToDTO(QueryDepartmentPO queryDepartmentPO);

    /**
     * 将 部门数据传输 DTO 对象 列表转换为 查询部门结果数据视图 VO 对象 列表
     *
     * @param departmentDTOList 部门数据传输 DTO 对象 列表 {@link List<DepartmentDTO>}
     * @return 查询部门结果数据视图 VO 对象 列表 {@link List<QueryDepartmentVO>}
     */
    List<QueryDepartmentVO> dtoListToQueryDepartmentVOList(List<DepartmentDTO> departmentDTOList);

    /**
     * 将 部门数据传输对象 转换为 部门数据实体类
     *
     * @param departmentDTO 部门数据传输对象 {@link DepartmentDTO}
     * @return 部门数据实体类 {@link Department}
     */
    Department departmentDTOToDepartment(DepartmentDTO departmentDTO);

    /**
     * 将 部门账户信息关联关系数据传输对象 转换为 部门账户信息关联关系数据实体类
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     * @return 部门账户信息关联关系数据实体类 {@link DepartmentAccountConnection}
     */
    DepartmentAccountConnection departmentAccountDTOToDepartmentAccountConnection(DepartmentAccountConnectionDTO departmentAccountConnectionDTO);

    /**
     * 将 部门数据实体类 转换为 部门数据传输对象
     *
     * @param department 部门数据实体类 {@link Department}
     * @return 部门数据传输对象 {@link DepartmentDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    DepartmentDTO departmentToDepartmentDTO(Department department);

    /**
     * 将 部门数据实体类 列表转换为 部门数据传输对象 列表
     *
     * @param departmentList 部门数据实体类 {@link  Department} 列表
     * @return 部门数据传输对象 列表 {@link List<DepartmentDTO>}
     */
    List<DepartmentDTO> departmentListToDepartmentDTOList(List<Department> departmentList);

}
