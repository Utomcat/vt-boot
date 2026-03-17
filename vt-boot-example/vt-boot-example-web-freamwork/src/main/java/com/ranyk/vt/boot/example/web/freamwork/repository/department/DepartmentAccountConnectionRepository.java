package com.ranyk.vt.boot.example.web.freamwork.repository.department;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.entity.DepartmentAccountConnection;
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
     * 根据数据ID删除单条部门账户关联关系数据
     *
     * @param id 部门账户关联关系ID
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteByIdEq(@Param("id") String id);

    /**
     * 根据数据ID列表批量删除部门账户关联关系数据
     *
     * @param ids 部门账户关联关系ID List 集合
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteByIdIn(@Param("ids") List<String> ids);

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

    /**
     * 根据部门ID和账户ID删除部门账户关联关系数据 - 物理删除数据
     *
     * @param departmentId 部门ID
     * @param accountId    账户ID
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteByDepartmentIdAndAccountId(@Param("departmentId") String departmentId, @Param("accountId") String accountId);

    /**
     * 根据账户ID删除部门账户关联关系数据 - 物理删除数据
     *
     * @param accountId 账户ID
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteByAccountId(@Param("accountId") String accountId);

    /**
     * 根据账户ID列表批量删除部门账户关联关系数据 - 物理删除数据
     *
     * @param accountIds 账户ID List 集合
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteByAccountIdIn(@Param("accountIds") List<String> accountIds);
}
