package com.ranyk.vt.boot.example.satoken.service.department;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.satoken.domain.department.entity.Department;
import com.ranyk.vt.boot.example.satoken.repository.department.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: DepartmentService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门业务逻辑类
 * @date: 2026-03-03
 */
@Slf4j
@Service
public class DepartmentService extends ServiceImpl<DepartmentRepository, Department> {

    /**
     * 部门信息数据操作接口对象
     */
    private final DepartmentRepository departmentRepository;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入部门信息数据操作接口对象
     *
     * @param departmentRepository 部门信息数据操作接口对象
     */
    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
}
