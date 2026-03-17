package com.ranyk.vt.boot.example.web.freamwork.repository.department;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: DepartmentRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门数据库操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface DepartmentRepository extends BaseMapper<Department> {

    /**
     * 根据部门 ID 查询一条部门信息
     *
     * @param id 部门 ID
     * @return 部门信息数据对象 {@link Department}
     */
    Department selectOneDepartmentById(@Param("id") String id);

    /**
     * 根据部门 ID 更新部门信息
     *
     * @param department 部门信息表实体对象 {@link Department}
     * @return 更新结果, true: 更新成功; false: 更新失败;
     */
    Boolean updateOneDepartmentById(@Param("department") Department department);

    /**
     * 根据条件 = name and != id , 查询数据数量
     *
     * @param name 部门名称
     * @param id 部门 ID
     * @return 部门信息数量
     */
    Integer selectCountByNameEqAndIdNotEq(@Param("name") String name, @Param("id") String id);

    /**
     * 根据条件 = code and != id , 获取数据数量
     *
     * @param code 部门编码
     * @param id 部门 ID
     * @return 部门信息数量
     */
    Integer selectCountByCodeEqAndIdNotEq(@Param("code") String code, @Param("id") String id);
}
