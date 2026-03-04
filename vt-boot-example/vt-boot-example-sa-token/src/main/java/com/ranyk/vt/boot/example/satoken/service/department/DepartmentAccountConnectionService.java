package com.ranyk.vt.boot.example.satoken.service.department;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.satoken.domain.department.entity.DepartmentAccountConnection;
import com.ranyk.vt.boot.example.satoken.repository.department.DepartmentAccountConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: DepartmentAccountConnectionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门账户信息关联关系业务逻辑类
 * @date: 2026-03-03
 */
@Slf4j
@Service
public class DepartmentAccountConnectionService extends ServiceImpl<DepartmentAccountConnectionRepository, DepartmentAccountConnection> {

    /**
     * 账户部门信息关联关系数据操作接口对象
     */
    private final DepartmentAccountConnectionRepository departmentAccountConnectionRepository;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入账户部门信息关联关系数据操作接口对象
     *
     * @param departmentAccountConnectionRepository 账户部门信息关联关系数据操作接口对象
     */
    @Autowired
    public DepartmentAccountConnectionService(DepartmentAccountConnectionRepository departmentAccountConnectionRepository) {
        this.departmentAccountConnectionRepository = departmentAccountConnectionRepository;
    }
}
