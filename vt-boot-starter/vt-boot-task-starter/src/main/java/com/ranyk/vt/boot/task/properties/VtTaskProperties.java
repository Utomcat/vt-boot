package com.ranyk.vt.boot.task.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS_NAME: VtTaskProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务配置属性类
 * <p>
 * 仅补充 Spring Boot 没有的能力: 总开关、任务覆盖策略、任务默认租户与声明式任务清单。
 * 线程池、存储类型(内存/JDBC)、集群等能力统一由 spring.quartz.* 配置。
 * </p>
 * @date: 2026-11-18
 */
@Data
@ConfigurationProperties(prefix = "vt.task")
public class VtTaskProperties {

    /**
     * 是否启用定时任务功能, 默认启用(引入即用)
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * 是否覆盖已存在的同名任务, 默认否; 开启后启动时会先删除旧任务再按当前定义重建
     */
    private Boolean overwriteExistingJobs = Boolean.FALSE;

    /**
     * 任务执行时使用的默认租户 ID, 为空表示不切换租户上下文
     */
    private String tenantId = "";

    /**
     * 声明式任务清单
     */
    private List<VtJobDefinition> jobs = new ArrayList<>();
}
