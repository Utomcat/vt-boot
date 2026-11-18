package com.ranyk.vt.boot.task.admin.domain.dto;

import com.ranyk.vt.boot.task.constant.MisfirePolicyEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * CLASS_NAME: VtJobSaveDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务保存参数 - 用于新增与修改任务
 * @date: 2026-11-18
 */
@Data
public class VtJobSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3180971548620698327L;

    /**
     * 任务名称
     */
    private String name;
    /**
     * 任务分组, 为空时使用 DEFAULT
     */
    private String group;
    /**
     * cron 表达式
     */
    private String cron;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 任务执行器 Bean 名称, 必须指向容器中实现了 VtJobExecutor 的 Bean
     */
    private String executor;
    /**
     * 错过触发的处理策略
     */
    private MisfirePolicyEnum misfire;
    /**
     * 任务参数
     */
    private Map<String, Object> params;
    /**
     * 租户 ID
     */
    private String tenantId;
}
