package com.ranyk.vt.boot.task.properties;

import com.ranyk.vt.boot.task.constant.MisfirePolicyEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * CLASS_NAME: VtJobDefinition.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 配置声明的任务定义 - 对应 vt.task.jobs 下的一项
 * @date: 2026-11-18
 */
@Data
public class VtJobDefinition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1795468238269153007L;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务分组, 默认 DEFAULT
     */
    private String group = "DEFAULT";

    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 任务描述
     */
    private String description = "";

    /**
     * 任务执行器 Bean 名称, 必须指向容器中实现了 VtJobExecutor 的 Bean
     */
    private String executor;

    /**
     * 错过触发的处理策略, 默认 DO_NOTHING
     */
    private MisfirePolicyEnum misfire = MisfirePolicyEnum.DO_NOTHING;

    /**
     * 任务执行时使用的租户 ID, 为空则取 vt.task.tenant-id
     */
    private String tenantId;

    /**
     * 任务参数
     */
    private Map<String, Object> params;
}
