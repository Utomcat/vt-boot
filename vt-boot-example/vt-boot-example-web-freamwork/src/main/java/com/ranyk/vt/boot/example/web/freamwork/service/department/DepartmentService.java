package com.ranyk.vt.boot.example.web.freamwork.service.department;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateType;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentAccountConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.entity.Department;
import com.ranyk.vt.boot.example.web.freamwork.mapper.department.DepartmentMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.department.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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
     * 部门信息数据转换接口对象
     */
    private final DepartmentMapper departmentMapper;
    /**
     * 部门账户信息关联关系业务逻辑类对象
     */
    private final DepartmentAccountConnectionService departmentAccountConnectionService;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入部门信息数据操作接口对象
     *
     * @param departmentRepository               部门信息数据操作接口对象
     * @param departmentMapper                   部门信息数据转换接口对象
     * @param departmentAccountConnectionService 部门账户信息关联关系业务逻辑类对象
     */
    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository,
                             DepartmentMapper departmentMapper,
                             DepartmentAccountConnectionService departmentAccountConnectionService) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.departmentAccountConnectionService = departmentAccountConnectionService;
    }

    /**
     * 新增部门信息 - 新增单条部门信息数据
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneDepartment(DepartmentDTO departmentDTO) {
        verifyDepartmentParams(departmentDTO, OperateType.SAVE);
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper.eq(Department::getName, departmentDTO.getName()).or().eq(Department::getCode, departmentDTO.getCode()));
        Long count = this.departmentRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("保存部门信息 - 部门名称: {} 或 部门编码: {} 已存在!", departmentDTO.getName(), departmentDTO.getCode());
            throw new ServiceException("保存部门信息 - 部门名称: %s 或 部门编码: %s 已存在!".formatted(departmentDTO.getName(), departmentDTO.getCode()));
        }
        Department department = departmentMapper.departmentDTOToDepartment(departmentDTO);
        department.setCreateBy(StpUtil.getLoginIdAsString());
        department.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean saveResult = saveOrUpdate(department);
        if (!saveResult) {
            log.error("保存部门信息 - 保存部门信息失败!");
            throw new ServiceException("保存部门信息 - 保存部门信息失败!");
        }
    }

    /**
     * 删除部门信息 - 依据部门数据ID删除单条部门信息数据
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneDepartment(DepartmentDTO departmentDTO) {
        // 验证当前需要删除的部门信息时是否存在需要删除的部门数据ID
        verifyDepartmentParams(departmentDTO, OperateType.DELETE);
        // 删除当前关联部门的账户信息
        Boolean deleteDepartmentAccountConnectionResult = departmentAccountConnectionService.deleteByDepartmentId(DepartmentAccountConnectionDTO.builder().departmentId(departmentDTO.getId()).build());
        if (!deleteDepartmentAccountConnectionResult) {
            log.error("依据 部门ID 删除所有该部门下的部门账户关联关系信息失败!");
            throw new ServiceException("依据 部门ID 删除所有该部门下的部门账户关联关系信息失败!");
        }
        Department department = departmentMapper.departmentDTOToDepartment(departmentDTO);
        department.setUpdateBy(StpUtil.getLoginIdAsString());
        // 执行部门数据的删除操作
        boolean deleteResult = removeById(department);
        if (!deleteResult) {
            log.error("删除部门信息 - 删除部门信息失败!");
            throw new ServiceException("删除部门信息 - 删除部门信息失败!");
        }
    }

    /**
     * 修改部门信息 - 依据部门数据ID修改单条部门信息数据
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneDepartment(DepartmentDTO departmentDTO) {
        verifyDepartmentParams(departmentDTO, OperateType.UPDATE);
        // 依据指定部门 ID 查询部门信息
        Department department = this.departmentRepository.selectOneDepartmentById(departmentDTO.getId());
        if (Objects.isNull(department)) {
            log.error("修改部门信息 - 需要进行部门信息修改数据不存在!");
            throw new ServiceException("修改部门信息 - 需要进行部门信息修改数据不存在!");
        }
        if (StrUtil.isNotBlank(departmentDTO.getName())) {
            Integer count = this.departmentRepository.selectCountByNameEqAndIdNotEq(departmentDTO.getName(), department.getId());
            if (count > 0) {
                log.error("修改部门信息 - 部门名称: {} 已存在!", departmentDTO.getName());
                throw new ServiceException("修改部门信息 - 部门名称: %s 已存在!".formatted(departmentDTO.getName()));
            }
            department.setName(departmentDTO.getName());
        }
        if (StrUtil.isNotBlank(departmentDTO.getCode())) {
            Integer count = this.departmentRepository.selectCountByCodeEqAndIdNotEq(departmentDTO.getCode(), department.getId());
            if (count > 0) {
                log.error("修改部门信息 - 部门编码: {} 已存在!", departmentDTO.getCode());
                throw new ServiceException("修改部门信息 - 部门编码: %s 已存在!".formatted(departmentDTO.getCode()));
            }
            department.setCode(departmentDTO.getCode());
        }
        Optional.ofNullable(departmentDTO.getParentId()).filter(StrUtil::isNotBlank).ifPresent(department::setParentId);
        Optional.ofNullable(departmentDTO.getParentIds()).filter(StrUtil::isNotBlank).ifPresent(department::setParentIds);
        Optional.ofNullable(departmentDTO.getRemark()).filter(StrUtil::isNotBlank).ifPresent(department::setRemark);
        Optional.ofNullable(departmentDTO.getStatus()).filter(item -> !Objects.equals(item, department.getStatus())).ifPresent(department::setStatus);
        department.setUpdateBy(StpUtil.getLoginIdAsString());
        department.setUpdateTime(LocalDateTime.now());
        boolean updateResult = this.departmentRepository.updateOneDepartmentById(department);
        if (!updateResult) {
            log.error("修改部门信息 - 修改部门信息失败!");
            throw new ServiceException("修改部门信息 - 修改部门信息失败!");
        }
    }

    /**
     * 查询部门信息
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     * @return 分页对象 {@link PageResponse} - 部门信息数据传输对象 {@link DepartmentDTO}
     */
    public PageResponse<DepartmentDTO> queryDepartment(DepartmentDTO departmentDTO) {
        IPage<Department> page = PageUtils.buildPage(departmentDTO);
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(departmentDTO.getName()), Department::getName, departmentDTO.getName());
        queryWrapper.like(StrUtil.isNotBlank(departmentDTO.getCode()), Department::getCode, departmentDTO.getCode());
        queryWrapper.like(StrUtil.isNotBlank(departmentDTO.getParentId()), Department::getParentId, departmentDTO.getParentId());
        queryWrapper.like(StrUtil.isNotBlank(departmentDTO.getParentIds()), Department::getParentIds, departmentDTO.getParentIds());
        queryWrapper.like(StrUtil.isNotBlank(departmentDTO.getRemark()), Department::getRemark, departmentDTO.getRemark());
        queryWrapper.eq(Objects.nonNull(departmentDTO.getStatus()), Department::getStatus, departmentDTO.getStatus());
        IPage<Department> departmentPage = departmentRepository.selectPage(page, queryWrapper);
        return PageUtils.buildPageResponse(departmentPage, departmentMapper.departmentListToDepartmentDTOList(departmentPage.getRecords()));
    }

    /**
     * 根据部门ID查询部门信息数量
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     * @return 部门信息数量
     */
    public Integer queryDepartmentCountById(DepartmentDTO departmentDTO) {
        if (StrUtil.isBlank(departmentDTO.getId())) {
            log.error("查询部门信息 - 部门ID不能为空!");
            throw new ServiceException("查询部门信息 - 部门ID不能为空!");
        }
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Department::getId, departmentDTO.getId());
        return Integer.parseInt(String.valueOf(departmentRepository.selectCount(queryWrapper)));
    }

    /**
     * 校验部门信息参数
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     * @param operateType   操作类型 {@link OperateType}
     */
    private void verifyDepartmentParams(DepartmentDTO departmentDTO, OperateType operateType) {
        switch (operateType) {
            case SAVE -> verifySaveDepartmentParams(departmentDTO);
            case DELETE -> verifyDeleteDepartmentParams(departmentDTO);
            case BATCH_DELETE -> verifyBatchDeleteDepartmentParams(departmentDTO);
            case UPDATE -> verifyUpdateDepartmentParams(departmentDTO);
            default -> throw new ServiceException("不支持的操作类型!");
        }
    }

    /**
     * 新增部门信息 - 校验部门信息参数
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     */
    private void verifySaveDepartmentParams(DepartmentDTO departmentDTO) {
        if (StrUtil.isBlank(departmentDTO.getName())) {
            log.error("新增部门信息参数校验失败 - 部门名称不能为空!");
            throw new ServiceException("新增部门信息参数校验失败 - 部门名称不能为空!");
        }
        if (StrUtil.isBlank(departmentDTO.getCode())) {
            log.error("新增部门信息参数校验失败 - 部门编码不能为空!");
            throw new ServiceException("新增部门信息参数校验失败 - 部门编码不能为空!");
        }
    }

    /**
     * 删除部门信息 - 校验部门信息参数
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     */
    private void verifyDeleteDepartmentParams(DepartmentDTO departmentDTO) {
        if (StrUtil.isBlank(departmentDTO.getId())) {
            log.error("删除部门信息参数校验失败 - 部门ID 不能为空!");
            throw new ServiceException("删除部门信息参数校验失败 - 部门ID 不能为空!");
        }
    }

    /**
     * 批量删除部门信息 - 校验部门信息参数
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     */
    private void verifyBatchDeleteDepartmentParams(DepartmentDTO departmentDTO) {
        if (CollectionUtil.isEmpty(departmentDTO.getIds())) {
            log.error("删除部门信息参数校验失败 - 部门ID 列表不能为空!");
            throw new ServiceException("删除部门信息参数校验失败 - 部门ID 列表不能为空!");
        }
    }

    /**
     * 修改部门信息 - 校验部门信息参数
     *
     * @param departmentDTO 部门信息数据传输对象 {@link DepartmentDTO}
     */
    private void verifyUpdateDepartmentParams(DepartmentDTO departmentDTO) {
        if (StrUtil.isBlank(departmentDTO.getId())) {
            log.error("修改部门信息 - 部门ID不能为空!");
            throw new ServiceException("修改部门信息 - 部门ID不能为空!");
        }
    }
}
