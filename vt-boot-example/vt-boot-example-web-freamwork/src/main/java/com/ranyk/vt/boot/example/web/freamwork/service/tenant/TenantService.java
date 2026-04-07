package com.ranyk.vt.boot.example.web.freamwork.service.tenant;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.dto.TenantDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.entity.Tenant;
import com.ranyk.vt.boot.example.web.freamwork.mapper.tenant.TenantMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.tenant.TenantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * CLASS_NAME: TenantService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户业务逻辑类
 * @date: 2026-04-06
 */
@Slf4j
@Service
public class TenantService extends ServiceImpl<TenantRepository, Tenant> {

    /**
     * 租户数据访问接口对象
     */
    private final TenantRepository tenantRepository;
    /**
     * 租户数据转换接口对象
     */
    private final TenantMapper tenantMapper;

    /**
     * 构造函数 - 向 Spring 容器中注册租户数据访问接口对象
     *
     * @param tenantRepository 租户数据访问接口对象
     * @param tenantMapper     租户数据转换接口对象
     */
    @Autowired
    public TenantService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    /**
     * 新增一条租户数据
     *
     * @param tenantDTO 租户信息数据传输对象 {@link TenantDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneTenant(TenantDTO tenantDTO) {
        verifyTenantParams(tenantDTO, OperateTypeEnum.SAVE);
        Tenant tenant = tenantMapper.tenantDTOToTenant(tenantDTO);
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tenant::getCode, tenantDTO.getCode());
        Long count = this.tenantRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("新增租户信息失败, 租户编码 {} 已存在", tenantDTO.getCode());
            throw new ServiceException("新增租户信息失败, 租户编码 【 %s 】 已存在".formatted(tenantDTO.getCode()));
        }
        tenant.setCreateBy(StpUtil.getLoginIdAsString());
        tenant.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean saveResult = this.saveOrUpdate(tenant);
        if (!saveResult) {
            log.error("新增租户信息失败");
            throw new ServiceException("新增租户信息失败");
        }
    }

    /**
     * 删除一条租户数据
     *
     * @param tenantDTO 租户信息数据传输对象 {@link TenantDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneTenant(TenantDTO tenantDTO) {
        verifyTenantParams(tenantDTO, OperateTypeEnum.DELETE);
        Tenant tenant = tenantMapper.tenantDTOToTenant(tenantDTO);
        tenant.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean deleteResult = removeById(tenant);
        if (!deleteResult) {
            log.error("删除租户信息失败");
            throw new ServiceException("删除租户信息失败");
        }
    }

    /**
     * 修改一条租户数据
     *
     * @param tenantDTO 租户信息数据传输对象 {@link TenantDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneTenant(TenantDTO tenantDTO) {
        verifyTenantParams(tenantDTO, OperateTypeEnum.UPDATE);
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tenant::getId, tenantDTO.getId());
        Tenant tenant = this.tenantRepository.selectOne(queryWrapper);
        if (StrUtil.isNotBlank(tenantDTO.getName())) {
            Integer count = this.tenantRepository.selectCountByNameEqAndIdNotEq(tenantDTO.getName(), tenant.getId());
            if (count > 0) {
                log.error("修改租户信息失败, 租户名称 {} 已存在", tenantDTO.getName());
                throw new ServiceException("修改租户信息失败, 租户名称 【 %s 】 已存在".formatted(tenantDTO.getName()));
            }
            tenant.setName(tenantDTO.getName());
        }
        if (StrUtil.isNotBlank(tenantDTO.getCode())) {
            Integer count = this.tenantRepository.selectCountByCodeEqAndIdNotEq(tenantDTO.getCode(), tenant.getId());
            if (count > 0) {
                log.error("修改租户信息失败, 租户编码 {} 已存在", tenantDTO.getCode());
                throw new ServiceException("修改租户信息失败, 租户编码 【 %s 】 已存在".formatted(tenantDTO.getCode()));
            }
            tenant.setCode(tenantDTO.getCode());
        }
        Optional.ofNullable(tenantDTO.getStatus()).ifPresent(tenant::setStatus);
        Optional.ofNullable(tenantDTO.getRemark()).ifPresent(tenant::setRemark);
        tenant.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean updateResult = this.saveOrUpdate(tenant);
        if (!updateResult) {
            log.error("修改租户信息失败!");
            throw new ServiceException("修改租户信息失败");
        }
    }

    /**
     * 根据条件查询租户信息
     *
     * @param tenantDTO 租户信息数据传输对象 {@link TenantDTO}
     * @return {@link PageResponse}租户信息数据传输对象列表 {@link TenantDTO}
     */
    public PageResponse<TenantDTO> queryTenantByConditions(TenantDTO tenantDTO) {
        Page<Tenant> page = PageUtils.buildPage(tenantDTO);
        LambdaQueryWrapper<Tenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(tenantDTO.getName()), Tenant::getName, tenantDTO.getName());
        queryWrapper.like(StrUtil.isNotBlank(tenantDTO.getCode()), Tenant::getCode, tenantDTO.getCode());
        queryWrapper.eq(Objects.nonNull(tenantDTO.getStatus()), Tenant::getStatus, tenantDTO.getStatus());
        queryWrapper.like(StrUtil.isNotBlank(tenantDTO.getRemark()), Tenant::getRemark, tenantDTO.getRemark());
        Page<Tenant> tenantPage = tenantRepository.selectPage(page, queryWrapper);
        return PageUtils.buildPageResponse(tenantPage, tenantMapper.tenantListToTenantDTOList(tenantPage.getRecords()));
    }

    /**
     * 验证租户信息数据传输对象参数
     *
     * @param tenantDTO   租户信息数据传输对象 {@link TenantDTO}
     * @param operateType 操作类型 {@link OperateTypeEnum}
     */
    private void verifyTenantParams(TenantDTO tenantDTO, OperateTypeEnum operateType) {
        switch (operateType) {
            case SAVE -> verifySaveTenantParams(tenantDTO);
            case UPDATE -> verifyUpdateTenantParams(tenantDTO);
            case DELETE -> verifyDeleteTenantParams(tenantDTO);
            default -> throw new ServiceException("不支持的操作类型");
        }
    }

    /**
     * 验证租户信息数据传输对象 - 保存操作
     *
     * @param tenantDTO 租户信息数据传输对象 {@link TenantDTO}
     */
    private void verifySaveTenantParams(TenantDTO tenantDTO) {
        if (StrUtil.isBlank(tenantDTO.getName())) {
            log.error("新增租户信息 - 租户名称不能为空");
            throw new ServiceException("新增租户信息 - 租户名称不能为空");
        }
        if (StrUtil.isBlank(tenantDTO.getCode())) {
            log.error("新增租户信息 - 租户编码不能为空");
            throw new ServiceException("新增租户信息 - 租户编码不能为空");
        }
    }

    /**
     * 验证租户信息数据传输对象 - 更新操作
     *
     * @param tenantDTO 租户信息数据传输对象 {@link TenantDTO}
     */
    private void verifyUpdateTenantParams(TenantDTO tenantDTO) {
        if (StrUtil.isBlank(tenantDTO.getId())) {
            log.error("更新租户信息 - 租户ID不能为空");
            throw new ServiceException("更新租户信息 - 租户ID不能为空");
        }
        if (StrUtil.isBlank(tenantDTO.getName())) {
            log.error("更新租户信息 - 租户名称不能为空");
            throw new ServiceException("更新租户信息 - 租户名称不能为空");
        }
        if (StrUtil.isBlank(tenantDTO.getCode())) {
            log.error("更新租户信息 - 租户编码不能为空");
            throw new ServiceException("更新租户信息 - 租户编码不能为空");
        }
    }

    /**
     * 验证租户信息数据传输对象 - 删除操作
     *
     * @param tenantDTO 租户信息数据传输对象 {@link TenantDTO}
     */
    private void verifyDeleteTenantParams(TenantDTO tenantDTO) {
        if (StrUtil.isBlank(tenantDTO.getId()) && CollUtil.isEmpty(tenantDTO.getIds())) {
            log.error("删除租户信息 - 租户ID或租户ID集合不能为空");
            throw new ServiceException("删除租户信息 - 租户ID或租户ID集合不能为空");
        }
    }

}
