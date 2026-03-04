package com.ranyk.vt.boot.example.satoken.repository.department;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.department.entity.Department;
import org.apache.ibatis.annotations.Mapper;
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
}
