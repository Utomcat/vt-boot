package com.ranyk.vt.boot.example.satoken.service.department;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateType;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.satoken.domain.department.dto.DepartmentAccountConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.department.entity.DepartmentAccountConnection;
import com.ranyk.vt.boot.example.satoken.repository.department.DepartmentAccountConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getDepartmentIds()} 属性
     * @return 新增结果, true: 新增成功, false: 新增失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByDepartmentId(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getDepartmentId()) && CollUtil.isEmpty(departmentAccountConnectionDTO.getDepartmentIds())) {
            log.error("依据部门ID 或 部门ID List 集合, 删除部门账户信息关联关系时, 部门ID => {} 和 部门ID List 集合 => {}不能同时为空!", departmentAccountConnectionDTO.getDepartmentId(), departmentAccountConnectionDTO.getDepartmentIds());
            throw new ServiceException("依据部门ID 或 部门ID List 集合, 删除部门账户信息关联关系时, 部门ID 和 部门ID List 集合 不能同时为空!");
        }
        Boolean deleteResult = Boolean.FALSE;
        // 删除部门账户信息关联关系 - 依据部门ID 删除部门账户关联关系数据
        if (StrUtil.isNotBlank(departmentAccountConnectionDTO.getDepartmentId())) {
            deleteResult = departmentAccountConnectionRepository.deleteByDepartmentId(departmentAccountConnectionDTO.getDepartmentId());
        }
        // 删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据
        if (CollUtil.isEmpty(departmentAccountConnectionDTO.getDepartmentIds())) {
            deleteResult = departmentAccountConnectionRepository.deleteByDepartmentIdIn(departmentAccountConnectionDTO.getDepartmentIds());
        }
        // 删除部门账户信息关联关系失败
        if (!deleteResult) {
            log.error("依据部门ID List 集合, 删除部门账户信息关联关系时, 删除失败!");
            throw new ServiceException("依据部门ID List 集合, 删除部门账户信息关联关系时, 删除失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * 参数校验 - 验证部门账户信息关联关系数据传输对象的参数是否符合要求, 对单表的增删改数据时进行参数校验
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     * @param operateType                    操作类型 {@link OperateType}
     */
    private void verifyDepartmentAccountConnectionParams(DepartmentAccountConnectionDTO departmentAccountConnectionDTO, OperateType operateType) {
        switch (operateType) {
            case SAVE -> verifySaveDepartmentAccountConnectionParams(departmentAccountConnectionDTO);
            case UPDATE -> verifyUpdateDepartmentAccountConnectionParams(departmentAccountConnectionDTO);
            case DELETE -> verifyDeleteDepartmentAccountConnectionParams(departmentAccountConnectionDTO);
            default -> throw new ServiceException("不支持的操作类型!");
        }
    }

    /**
     * 新增部门和账户的关联关系时, 参数校验
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     */
    private void verifySaveDepartmentAccountConnectionParams(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getDepartmentId()) || StrUtil.isBlank(departmentAccountConnectionDTO.getAccountId())) {
            log.error("新增部门和账户的关联关系时, 部门ID => {} 和 账户ID => {} 均不能为空!", departmentAccountConnectionDTO.getDepartmentId(), departmentAccountConnectionDTO.getAccountId());
            throw new ServiceException("新增部门和账户的关联关系时, 部门ID 和 账户ID 均不能为空!");
        }
    }

    /**
     * 更部门和账户的关联关系时, 参数校验
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     */
    private void verifyUpdateDepartmentAccountConnectionParams(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getId())) {
            log.error("更新部门和账户的关联关系时, 部门账户信息关联关系ID不能为空!");
            throw new ServiceException("更新部门和账户的关联关系时, 部门账户信息关联关系ID不能为空!");
        }
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getDepartmentId()) && StrUtil.isBlank(departmentAccountConnectionDTO.getAccountId())) {
            log.error("更新部门和账户的关联关系时, 部门ID => {} 和 账户ID => {} 不能同时为空!", departmentAccountConnectionDTO.getDepartmentId(), departmentAccountConnectionDTO.getAccountId());
            throw new ServiceException("更新部门和账户的关联关系时, 部门ID 和 账户ID 不能同时为空!");
        }
    }

    /**
     * 删除部门和账户的关联关系时, 参数校验
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     */
    private void verifyDeleteDepartmentAccountConnectionParams(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getId()) && CollUtil.isEmpty(departmentAccountConnectionDTO.getIds())) {
            throw new ServiceException("部门账户信息关联关系ID不能为空!");
        }
    }

}
