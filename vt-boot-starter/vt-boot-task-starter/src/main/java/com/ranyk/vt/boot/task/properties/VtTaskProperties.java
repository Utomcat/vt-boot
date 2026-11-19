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
 * 线程池、集群等能力统一由 spring.quartz.* 配置; 存储默认由 {@code VtTaskStoreAutoConfiguration}
 * 装配为 JDBC(任务持久化到数据库, 服务重启不丢失), 需要退回内存存储时配置 vt.task.store.type=memory。
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
     * 是否覆盖已存在的同名任务, 默认否
     * <p>
     * 默认否: 重启后保留作业存储中已持久化的任务(含暂停状态与运行期修改), 注册器发现同名任务直接跳过;
     * 修改了注解或配置中的 cron 需要同步到数据库时, 再将此项置为 true (启动时先删除旧任务再按当前定义重建)。
     * </p>
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
