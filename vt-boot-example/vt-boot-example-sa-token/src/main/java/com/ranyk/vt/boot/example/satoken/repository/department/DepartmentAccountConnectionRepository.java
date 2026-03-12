package com.ranyk.vt.boot.example.satoken.repository.department;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.department.entity.DepartmentAccountConnection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CLASS_NAME: DepartmentAccountConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库部门账户关联关系表操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface DepartmentAccountConnectionRepository extends BaseMapper<DepartmentAccountConnection> {

    /**
     * 根据部门ID批量删除部门账户关联关系数据 - 物理删除数据
     *
     * @param departmentId 部门ID
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteByDepartmentId(@Param("departmentId") String departmentId);

    /**
     * 根据部门ID列表批量删除部门账户关联关系数据 - 物理删除数据
     *
     * @param departmentIds 部门ID List 集合
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteByDepartmentIdIn(@Param("departmentIds") List<String> departmentIds);
}
