package com.ranyk.vt.boot.example.system.tenant.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.system.tenant.converter.SysTenantConverter;
import com.ranyk.vt.boot.example.system.tenant.domain.dto.TenantFormDTO;
import com.ranyk.vt.boot.example.system.tenant.domain.dto.TenantQueryDTO;
import com.ranyk.vt.boot.example.system.tenant.domain.entity.SysTenant;
import com.ranyk.vt.boot.example.system.tenant.domain.vo.TenantVO;
import com.ranyk.vt.boot.example.system.tenant.mapper.SysTenantMapper;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CLASS_NAME: SysTenantService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户管理服务
 * @date: 2026-11-16
 */
@Service
@RequiredArgsConstructor
public class SysTenantService {

    /**
     * 默认套餐
     */
    private static final String DEFAULT_PLAN = "FREE";
    /**
     * 状态: 正常
     */
    private static final int STATUS_NORMAL = 1;

    /**
     * 租户数据访问对象
     */
    private final SysTenantMapper sysTenantMapper;
    /**
     * 租户实体转换器
     */
    private final SysTenantConverter sysTenantConverter;

    /**
     * 按租户名称、编码、套餐和状态等条件分页查询租户。查询条件为空时自动忽略,
     * 结果按创建时间倒序排列, 并转换成前端展示所需的租户视图对象。
     *
     * @param queryDTO 分页查询参数, 包含页码、每页条数及可选筛选条件
     * @return 包含当前页租户列表与总数、页码、页大小的分页结果
     */
    public MultiResult<TenantVO> page(TenantQueryDTO queryDTO) {
        Page<SysTenant> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<SysTenant> wrapper = Wrappers.<SysTenant>lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getName()), SysTenant::getName, queryDTO.getName())
                .like(StrUtil.isNotBlank(queryDTO.getCode()), SysTenant::getCode, queryDTO.getCode())
                .eq(StrUtil.isNotBlank(queryDTO.getPlan()), SysTenant::getPlan, queryDTO.getPlan())
                .eq(ObjectUtil.isNotNull(queryDTO.getStatus()), SysTenant::getStatus, queryDTO.getStatus())
                .orderByDesc(SysTenant::getCreateTime);
        IPage<SysTenant> result = sysTenantMapper.selectPage(page, wrapper);
        List<TenantVO> list = result.getRecords().stream().map(sysTenantConverter::toVO).toList();
        return MultiResult.successMulti(list, result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    /**
     * 根据租户ID查询租户详情, 租户不存在时抛出业务异常。
     *
     * @param id 租户ID
     * @return 租户详细信息
     */
    public TenantVO detail(Long id) {
        return sysTenantConverter.toVO(getExistTenant(id));
    }

    /**
     * 新增租户。会校验租户名称和编码必填、编码全局唯一; 未指定的套餐、用户数上限、
     * 存储上限和每日Token上限会使用系统默认值。
     *
     * @param formDTO 租户表单参数
     * @return 新增租户的ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(TenantFormDTO formDTO) {
        if (StrUtil.isBlank(formDTO.getName())) {
            throw new ServiceException("租户名称不能为空");
        }
        if (StrUtil.isBlank(formDTO.getCode())) {
            throw new ServiceException("租户编码不能为空");
        }
        Long count = sysTenantMapper.selectCount(
                Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getCode, formDTO.getCode()));
        if (count > 0) {
            throw new ServiceException("租户编码已存在");
        }
        // 表单转实体后, 补齐默认值
        SysTenant tenant = sysTenantConverter.toEntity(formDTO);
        tenant.setPlan(StrUtil.blankToDefault(formDTO.getPlan(), DEFAULT_PLAN));
        tenant.setMaxUsers(ObjectUtil.isNull(formDTO.getMaxUsers()) ? 10 : formDTO.getMaxUsers());
        tenant.setMaxStorageMb(ObjectUtil.isNull(formDTO.getMaxStorageMb()) ? 1024L : formDTO.getMaxStorageMb());
        tenant.setMaxDailyTokens(ObjectUtil.isNull(formDTO.getMaxDailyTokens()) ? 1000000L : formDTO.getMaxDailyTokens());
        tenant.setStatus(ObjectUtil.isNull(formDTO.getStatus()) ? STATUS_NORMAL : formDTO.getStatus());
        sysTenantMapper.insert(tenant);
        return tenant.getId();
    }

    /**
     * 修改租户信息。仅更新非空字段, 编码作为唯一标识不允许在此修改,
     * 避免破坏租户编码与其他数据的关联关系。
     *
     * @param formDTO 租户表单参数, 必须包含租户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(TenantFormDTO formDTO) {
        if (ObjectUtil.isNull(formDTO.getId())) {
            throw new ServiceException("租户ID不能为空");
        }
        SysTenant tenant = getExistTenant(formDTO.getId());
        sysTenantConverter.updateEntity(formDTO, tenant);
        sysTenantMapper.updateById(tenant);
    }

    /**
     * 逻辑删除租户。删除前先确认租户存在, 底层使用逻辑删除仅标记 deleted,
     * 保留租户及其关联数据用于审计追溯。
     *
     * @param id 租户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        getExistTenant(id);
        sysTenantMapper.deleteById(id);
    }

    /**
     * 修改租户状态(启用/禁用)。状态值不能为空, 修改前会确认租户存在。
     *
     * @param id     租户ID
     * @param status 目标状态: 1-正常 0-禁用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        if (ObjectUtil.isNull(status)) {
            throw new ServiceException("状态不能为空");
        }
        SysTenant tenant = getExistTenant(id);
        tenant.setStatus(status);
        sysTenantMapper.updateById(tenant);
    }

    /**
     * 获取存在的租户
     *
     * @param id 租户ID
     * @return 租户实体 {@link SysTenant}
     */
    private SysTenant getExistTenant(Long id) {
        SysTenant tenant = sysTenantMapper.selectById(id);
        if (ObjectUtil.isNull(tenant)) {
            throw new ServiceException("租户不存在");
        }
        return tenant;
    }
}
