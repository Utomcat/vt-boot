package com.ranyk.vt.boot.example.web.freamwork.service.department;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateType;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentAccountConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.entity.DepartmentAccountConnection;
import com.ranyk.vt.boot.example.web.freamwork.mapper.department.DepartmentMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.department.DepartmentAccountConnectionRepository;
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
     * 部门信息数据操作接口对象
     */
    private final DepartmentMapper departmentMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入账户部门信息关联关系数据操作接口对象
     *
     * @param departmentAccountConnectionRepository 账户部门信息关联关系数据操作接口对象
     * @param departmentMapper                      部门信息数据操作接口对象
     */
    @Autowired
    public DepartmentAccountConnectionService(DepartmentAccountConnectionRepository departmentAccountConnectionRepository, DepartmentMapper departmentMapper) {
        this.departmentAccountConnectionRepository = departmentAccountConnectionRepository;
        this.departmentMapper = departmentMapper;
    }

    /**
     * 新增部门账户信息关联关系数据 - 新增单条部门账户信息关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getDepartmentId()} 和 {@link DepartmentAccountConnectionDTO#getAccountId()} 属性
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneDepartmentAccountConnection(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        verifyDepartmentAccountConnectionParams(departmentAccountConnectionDTO, OperateType.SAVE);
        LambdaQueryWrapper<DepartmentAccountConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq(DepartmentAccountConnection::getDepartmentId, departmentAccountConnectionDTO.getDepartmentId()).eq(DepartmentAccountConnection::getAccountId, departmentAccountConnectionDTO.getAccountId()));
        Long count = this.departmentAccountConnectionRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("新增部门账户信息关联关系数据 - 保存一条部门账户信息关联关系数据 - 部门账户信息关联关系已存在!");
            throw new ServiceException("新增部门账户信息关联关系数据 - 保存一条部门账户信息关联关系数据 - 部门账户信息关联关系已存在!");
        }
        DepartmentAccountConnection departmentAccountConnection = departmentMapper.departmentAccountDTOToDepartmentAccountConnection(departmentAccountConnectionDTO);
        departmentAccountConnection.setCreateBy(StpUtil.getLoginIdAsString());
        departmentAccountConnection.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean saveResult = this.saveOrUpdate(departmentAccountConnection);
        if (!saveResult) {
            log.error("保存部门账户信息关联关系 - 保存部门账户信息关联关系失败!");
            throw new ServiceException("保存部门账户信息关联关系 - 保存部门账户信息关联关系失败!");
        }
    }

    /**
     * 删除部门账户信息关联关系 - 依据数据ID 删除单条部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getId()} 属性
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneDepartmentAccountConnection(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        verifyDepartmentAccountConnectionParams(departmentAccountConnectionDTO, OperateType.DELETE);
        Boolean deleteResult = this.departmentAccountConnectionRepository.deleteByIdEq(departmentAccountConnectionDTO.getId());
        if (!deleteResult) {
            log.error("删除部门账户信息关联关系 - 依据数据ID,删除单条部门账户信息关联关系失败!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据数据ID,删除单条部门账户信息关联关系失败!");
        }
    }

    /**
     * 批量删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getIds()} 属性
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDepartmentAccountConnection(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        verifyDepartmentAccountConnectionParams(departmentAccountConnectionDTO, OperateType.BATCH_DELETE);
        Boolean deleteResult = this.departmentAccountConnectionRepository.deleteByIdIn(departmentAccountConnectionDTO.getIds());
        if (!deleteResult) {
            log.error("批量删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据失败!");
            throw new ServiceException("批量删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据失败!");
        }
    }

    /**
     * 删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getDepartmentIds()} 属性
     * @return 新增结果, true: 新增成功, false: 新增失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByDepartmentId(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getDepartmentId())) {
            log.error("删除部门账户信息关联关系 - 依据部门ID 删除部门账户关联关系数据时, 部门ID 不能为空!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据部门ID 删除部门账户关联关系数据时, 部门ID 不能为空!");
        }
        // 删除部门账户信息关联关系 - 依据部门ID 删除部门账户关联关系数据
        Boolean deleteResult = departmentAccountConnectionRepository.deleteByDepartmentId(departmentAccountConnectionDTO.getDepartmentId());
        if (!deleteResult) {
            log.error("删除部门账户信息关联关系 - 依据部门ID 删除部门账户关联关系数据时, 删除失败!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据部门ID 删除部门账户关联关系数据时, 删除失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * 删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getDepartmentIds()} 属性
     * @return 新增结果, true: 新增成功, false: 新增失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByDepartmentIds(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (CollectionUtil.isEmpty(departmentAccountConnectionDTO.getDepartmentIds())) {
            log.error("删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据时, 部门ID List 集合不能为空!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据时, 部门ID List 集合不能为空!");
        }
        // 删除部门账户信息关联关系 - 依据部门ID List 集合批量删除部门账户关联关系数据
        Boolean deleteResult = departmentAccountConnectionRepository.deleteByDepartmentIdIn(departmentAccountConnectionDTO.getDepartmentIds());
        if (!deleteResult) {
            log.error("依据部门ID List 集合, 删除部门账户信息关联关系时, 删除失败!");
            throw new ServiceException("依据部门ID List 集合, 删除部门账户信息关联关系时, 删除失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * 删除部门账户信息关联关系 - 依据部门ID 和 账户ID 删除部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getDepartmentId()} 和 {@link DepartmentAccountConnectionDTO#getAccountId()} 属性
     * @return 新增结果, true: 新增成功, false: 新增失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByDepartmentIdAndAccountId(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getDepartmentId()) || StrUtil.isBlank(departmentAccountConnectionDTO.getAccountId())) {
            log.error("依据部门ID 和 账户ID, 删除部门账户信息关联关系时, 部门ID => {} 或 账户ID => {} 存在一个为空为空!", departmentAccountConnectionDTO.getDepartmentId(), departmentAccountConnectionDTO.getAccountId());
            throw new ServiceException("依据部门ID 和 账户ID, 删除部门账户信息关联关系时, 部门ID 或 账户ID 存在一个为空为空!");
        }
        Boolean deleteResult;
        // 删除部门账户信息关联关系 - 依据部门ID 删除部门账户关联关系数据
        deleteResult = departmentAccountConnectionRepository.deleteByDepartmentIdAndAccountId(departmentAccountConnectionDTO.getDepartmentId(), departmentAccountConnectionDTO.getAccountId());
        if (!deleteResult) {
            log.error("依据部门ID 和 账户ID, 删除部门账户信息关联关系时, 删除失败!");
            throw new ServiceException("依据部门ID 和 账户ID, 删除部门账户信息关联关系时, 删除失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * 删除部门账户信息关联关系 - 依据账户ID 删除部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getAccountId()} 账户ID 属性
     * @return 新增结果, true: 新增成功, false: 新增失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByAccountId(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getAccountId())) {
            log.error("删除部门账户信息关联关系 - 依据账户ID, 删除部门账户信息关联关系时, 账户ID 为空!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据账户ID, 删除部门账户信息关联关系时, 账户ID 为空!");
        }
        Boolean deleteResult = departmentAccountConnectionRepository.deleteByAccountId(departmentAccountConnectionDTO.getAccountId());
        if (!deleteResult) {
            log.error("删除部门账户信息关联关系 - 依据账户ID, 删除部门账户信息关联关系时, 删除失败!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据账户ID, 删除部门账户信息关联关系时, 删除失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * 删除部门账户信息关联关系 - 依据账户ID List 集合批量删除部门账户关联关系数据
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象, 主要使用 {@link DepartmentAccountConnectionDTO#getAccountIds()} 账户ID List 集合属性
     * @return 新增结果, true: 新增成功, false: 新增失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByAccountIds(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (CollUtil.isEmpty(departmentAccountConnectionDTO.getAccountIds())) {
            log.error("删除部门账户信息关联关系 - 依据账户ID List 集合, 删除部门账户信息关联关系时, 账户ID List 集合为空!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据账户ID List 集合, 删除部门账户信息关联关系时, 账户ID List 集合为空!");
        }
        Boolean deleteResult = departmentAccountConnectionRepository.deleteByAccountIdIn(departmentAccountConnectionDTO.getAccountIds());
        if (!deleteResult) {
            log.error("删除部门账户信息关联关系 - 依据账户ID List 集合, 删除部门账户信息关联关系时, 删除失败!");
            throw new ServiceException("删除部门账户信息关联关系 - 依据账户ID List 集合, 删除部门账户信息关联关系时, 删除失败!");
        }
        return Boolean.TRUE;
    }

    /**
     * 部门账户信息关联关系参数校验
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     * @param operateType                    操作类型 {@link OperateType}
     */
    private void verifyDepartmentAccountConnectionParams(DepartmentAccountConnectionDTO departmentAccountConnectionDTO, OperateType operateType) {
        switch (operateType) {
            case SAVE -> verifySaveDepartmentAccountConnectionParams(departmentAccountConnectionDTO);
            case DELETE -> verifyDeleteDepartmentAccountConnectionParams(departmentAccountConnectionDTO);
            case BATCH_DELETE -> verifyBatchDeleteDepartmentAccountConnectionParams(departmentAccountConnectionDTO);
            default -> throw new ServiceException("不支持的操作类型!");
        }
    }

    /**
     * 部门账户信息关联关系新增参数校验 - 新增部单条门和账户的关联关系数据参数校验
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
     * 部门账户信息关联关系删除参数校验 - 删除单条部门和账户的关联关系数据参数校验
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     */
    private void verifyDeleteDepartmentAccountConnectionParams(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (StrUtil.isBlank(departmentAccountConnectionDTO.getId())) {
            log.error("删除部门账户信息关联关系时, 需要删除的数据ID 不能为空!");
            throw new ServiceException("删除部门账户信息关联关系时, 需要删除的数据ID 不能为空!");
        }
    }

    /**
     * 部门账户信息关联关系删除参数校验 - 批量删除部门和账户的关联关系数据参数校验
     *
     * @param departmentAccountConnectionDTO 部门账户信息关联关系数据传输对象 {@link DepartmentAccountConnectionDTO}
     */
    private void verifyBatchDeleteDepartmentAccountConnectionParams(DepartmentAccountConnectionDTO departmentAccountConnectionDTO) {
        if (CollectionUtil.isEmpty(departmentAccountConnectionDTO.getIds())) {
            log.error("批量删除部门账户信息关联关系时, 数据ID List 集合 不能为空!");
            throw new ServiceException("批量删除部门账户信息关联关系时, 数据ID List 集合 不能为空!");
        }
    }

}
