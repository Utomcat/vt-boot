package com.ranyk.vt.boot.task.handle;

import com.ranyk.vt.boot.task.annotation.VtJob;
import com.ranyk.vt.boot.task.properties.VtJobDefinition;
import com.ranyk.vt.boot.task.properties.VtTaskProperties;
import com.ranyk.vt.boot.task.util.VtTaskUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.EverythingMatcher;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.List;
import java.util.Map;

/**
 * CLASS_NAME: VtJobRegistrar.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务注册器 - 启动时把 @VtJob 注解任务与配置声明的任务注册到 Quartz
 * <p>
 * 注册失败(例如 cron 非法)只记录错误日志, 不阻断应用启动, 避免单个任务配置错误拖垮整个应用。
 * 运行期的增删改由 {@link VtTaskUtils} 承担, 两者共用同一套注册逻辑。
 * </p>
 * <p>
 * 框架默认使用 JDBC 存储, 任务在重启后依然存在: 未开启 overwrite-existing-jobs 时,
 * 已存在于作业存储中的同名任务会被保留(包含暂停状态与运行期修改), 直接跳过注册而非报错。
 * </p>
 * @date: 2026-11-18
 */
@Slf4j
@RequiredArgsConstructor
public class VtJobRegistrar implements SmartInitializingSingleton {

    /**
     * 调度器对象
     */
    private final Scheduler scheduler;

    /**
     * 任务配置属性对象
     */
    private final VtTaskProperties taskProperties;

    /**
     * Spring 应用上下文对象
     */
    private final ApplicationContext applicationContext;

    /**
     * 任务监听器列表 - 为空表示不记录执行日志
     */
    private final List<VtJobListener> jobListeners;

    /**
     * 容器初始化完成后注册任务
     */
    @Override
    public void afterSingletonsInstantiated() {
        registerListeners();
        registerAnnotationJobs();
        registerDeclaredJobs();
        log.debug("VtJob registrar finished. scheduler: {}", safeSchedulerName());
    }

    /**
     * 向调度器注册任务监听器
     */
    private void registerListeners() {
        for (VtJobListener listener : jobListeners) {
            try {
                scheduler.getListenerManager().addJobListener(listener, EverythingMatcher.allJobs());
                log.debug("VtJob listener registered. listener: {}", listener.getName());
            } catch (SchedulerException e) {
                log.error("VtJob listener register failed. listener: {}", listener.getName(), e);
            }
        }
    }

    /**
     * 注册标注了 {@link VtJob} 的任务
     */
    private void registerAnnotationJobs() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(VtJob.class);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            VtJob annotation = AnnotationUtils.findAnnotation(entry.getValue().getClass(), VtJob.class);
            if (annotation == null) {
                continue;
            }
            VtJobDefinition definition = new VtJobDefinition();
            definition.setName(annotation.name().isBlank() ? entry.getKey() : annotation.name());
            definition.setGroup(annotation.group());
            definition.setCron(annotation.cron());
            definition.setDescription(annotation.description());
            definition.setExecutor(entry.getKey());
            definition.setMisfire(annotation.misfire());
            doRegister(definition);
        }
    }

    /**
     * 注册配置中声明的任务
     */
    private void registerDeclaredJobs() {
        for (VtJobDefinition definition : taskProperties.getJobs()) {
            doRegister(definition);
        }
    }

    /**
     * 执行单个任务注册 - 失败只记录日志, 不阻断启动
     *
     * @param definition 任务定义 {@link VtJobDefinition}
     */
    private void doRegister(VtJobDefinition definition) {
        boolean overwrite = Boolean.TRUE.equals(taskProperties.getOverwriteExistingJobs());
        try {
            if (!overwrite && VtTaskUtils.exists(definition.getGroup(), definition.getName())) {
                log.debug("VtJob already exists in job store, keep persisted definition. task: {}, group: {}",
                        definition.getName(), definition.getGroup());
                return;
            }
            VtTaskUtils.addOrUpdate(definition, overwrite);
            log.debug("VtJob registered. task: {}, group: {}, cron: {}",
                    definition.getName(), definition.getGroup(), definition.getCron());
        } catch (Exception e) {
            log.error("VtJob register failed. task: {}, group: {}, cron: {}",
                    definition.getName(), definition.getGroup(), definition.getCron(), e);
        }
    }

    /**
     * 获取调度器名称 - 仅用于日志, 获取失败返回 unknown
     *
     * @return 调度器名称
     */
    private String safeSchedulerName() {
        try {
            return scheduler.getSchedulerName();
        } catch (SchedulerException e) {
            return "unknown";
        }
    }
}
