package com.ranyk.vt.boot.task.admin.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.task.admin.domain.dto.VtJobQueryDTO;
import com.ranyk.vt.boot.task.admin.domain.dto.VtJobSaveDTO;
import com.ranyk.vt.boot.task.admin.domain.vo.VtJobVO;
import com.ranyk.vt.boot.task.api.VtJobExecutor;
import com.ranyk.vt.boot.task.constant.MisfirePolicyEnum;
import com.ranyk.vt.boot.task.properties.VtJobDefinition;
import com.ranyk.vt.boot.task.util.VtTaskUtils;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * CLASS_NAME: VtJobService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务管理业务类 - 对 {@link VtTaskUtils} 做面向接口的封装与校验
 * @date: 2026-11-18
 */
@Slf4j
public class VtJobService {

    /**
     * 分页查询任务列表
     *
     * @param queryDTO 分页查询参数
     * @return 包含当前页任务列表与总数的分页结果
     */
    public MultiResult<VtJobVO> page(VtJobQueryDTO queryDTO) {
        int page = normalizePage(queryDTO.getPage());
        int size = normalizeSize(queryDTO.getSize());
        List<VtJobVO> all = VtTaskUtils.list().stream()
                .filter(item -> match(item, queryDTO))
                .map(this::toVO)
                .toList();
        int total = all.size();
        int fromIndex = Math.min((page - 1) * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        return MultiResult.successMulti(all.subList(fromIndex, toIndex), total, page, size);
    }

    /**
     * 查询任务详情
     *
     * @param group 任务分组
     * @param name  任务名称
     * @return 任务信息
     */
    public VtJobVO detail(String group, String name) {
        VtJobDefinition definition = VtTaskUtils.get(group, name);
        if (definition == null) {
            throw new ServiceException("任务不存在: " + name);
        }
        return toVO(definition);
    }

    /**
     * 新增任务
     *
     * @param saveDTO 任务保存参数
     */
    public void create(VtJobSaveDTO saveDTO) {
        validate(saveDTO);
        if (VtTaskUtils.exists(saveDTO.getGroup(), saveDTO.getName())) {
            throw new ServiceException("任务已存在: " + saveDTO.getName());
        }
        VtTaskUtils.addOrUpdate(toDefinition(saveDTO), false);
    }

    /**
     * 修改任务 - 已存在的任务会被覆盖重建
     *
     * @param saveDTO 任务保存参数
     */
    public void update(VtJobSaveDTO saveDTO) {
        validate(saveDTO);
        if (!VtTaskUtils.exists(saveDTO.getGroup(), saveDTO.getName())) {
            throw new ServiceException("任务不存在: " + saveDTO.getName());
        }
        VtTaskUtils.addOrUpdate(toDefinition(saveDTO), true);
    }

    /**
     * 暂停任务
     *
     * @param group 任务分组
     * @param name  任务名称
     */
    public void pause(String group, String name) {
        VtTaskUtils.pause(group, name);
    }

    /**
     * 恢复任务
     *
     * @param group 任务分组
     * @param name  任务名称
     */
    public void resume(String group, String name) {
        VtTaskUtils.resume(group, name);
    }

    /**
     * 立即执行一次
     *
     * @param group 任务分组
     * @param name  任务名称
     */
    public void triggerNow(String group, String name) {
        VtTaskUtils.triggerNow(group, name);
    }

    /**
     * 删除任务
     *
     * @param group 任务分组
     * @param name  任务名称
     */
    public void delete(String group, String name) {
        if (!VtTaskUtils.delete(group, name)) {
            throw new ServiceException("任务不存在: " + name);
        }
    }

    /**
     * 校验参数完整性
     *
     * @param saveDTO 任务保存参数
     */
    private void validate(VtJobSaveDTO saveDTO) {
        if (StrUtil.isBlank(saveDTO.getName())) {
            throw new ServiceException("任务名称不能为空");
        }
        if (StrUtil.isBlank(saveDTO.getCron())) {
            throw new ServiceException("cron 表达式不能为空");
        }
        if (StrUtil.isBlank(saveDTO.getExecutor())) {
            throw new ServiceException("任务执行器不能为空");
        }
        try {
            SpringUtil.getBean(saveDTO.getExecutor(), VtJobExecutor.class);
        } catch (Exception e) {
            throw new ServiceException("任务执行器不存在或未实现 VtJobExecutor: " + saveDTO.getExecutor());
        }
    }

    /**
     * 把保存参数转换为任务定义
     *
     * @param saveDTO 任务保存参数
     * @return 任务定义 {@link VtJobDefinition}
     */
    private VtJobDefinition toDefinition(VtJobSaveDTO saveDTO) {
        VtJobDefinition definition = new VtJobDefinition();
        definition.setName(saveDTO.getName());
        definition.setGroup(saveDTO.getGroup());
        definition.setCron(saveDTO.getCron());
        definition.setDescription(saveDTO.getDescription());
        definition.setExecutor(saveDTO.getExecutor());
        definition.setMisfire(saveDTO.getMisfire() == null ? MisfirePolicyEnum.DO_NOTHING : saveDTO.getMisfire());
        definition.setParams(saveDTO.getParams());
        definition.setTenantId(saveDTO.getTenantId());
        return definition;
    }

    /**
     * 把任务定义转换为响应对象
     *
     * @param definition 任务定义 {@link VtJobDefinition}
     * @return 任务信息 {@link VtJobVO}
     */
    private VtJobVO toVO(VtJobDefinition definition) {
        return VtJobVO.builder()
                .name(definition.getName())
                .group(definition.getGroup())
                .cron(definition.getCron())
                .description(definition.getDescription())
                .executor(definition.getExecutor())
                .misfire(definition.getMisfire())
                .params(definition.getParams())
                .tenantId(definition.getTenantId())
                .paused(VtTaskUtils.isPaused(definition.getGroup(), definition.getName()))
                .build();
    }

    /**
     * 判断任务是否匹配查询条件
     *
     * @param definition 任务定义 {@link VtJobDefinition}
     * @param queryDTO   分页查询参数
     * @return true: 匹配; false: 不匹配
     */
    private boolean match(VtJobDefinition definition, VtJobQueryDTO queryDTO) {
        if (StrUtil.isNotBlank(queryDTO.getName())
                && !Objects.requireNonNullElse(definition.getName(), "").contains(queryDTO.getName())) {
            return false;
        }
        return StrUtil.isBlank(queryDTO.getGroup()) || queryDTO.getGroup().equals(definition.getGroup());
    }

    /**
     * 归一化页码
     *
     * @param page 页码
     * @return 归一化后的页码
     */
    private int normalizePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    /**
     * 归一化每页条数
     *
     * @param size 每页条数
     * @return 归一化后的每页条数
     */
    private int normalizeSize(Integer size) {
        if (size == null || size < 1) {
            return 10;
        }
        return Math.min(size, 200);
    }
}
