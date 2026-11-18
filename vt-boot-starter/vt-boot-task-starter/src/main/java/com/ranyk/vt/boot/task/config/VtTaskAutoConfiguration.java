package com.ranyk.vt.boot.task.config;

import com.ranyk.vt.boot.task.handle.VtJobListener;
import com.ranyk.vt.boot.task.handle.VtJobRegistrar;
import com.ranyk.vt.boot.task.properties.VtTaskProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.quartz.autoconfigure.QuartzAutoConfiguration;
import org.springframework.boot.quartz.autoconfigure.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: VtTaskAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务自动配置类 - 基于 Quartz, 补充任务覆盖策略与任务注册能力
 * <p>
 * 线程池、存储类型(内存/JDBC)、集群等能力统一由 spring.quartz.* 配置, 框架不重复造轮子。
 * 执行日志相关能力见 {@link VtTaskLogAutoConfiguration}。
 * </p>
 * @date: 2026-11-18
 */
@Slf4j
@AutoConfiguration(after = QuartzAutoConfiguration.class)
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBeanCustomizer.class})
@EnableConfigurationProperties(VtTaskProperties.class)
@ConditionalOnBooleanProperty(prefix = "vt.task", name = "enabled", matchIfMissing = true)
public class VtTaskAutoConfiguration {

    /**
     * 注册 Quartz 工厂定制器 - 按配置决定是否覆盖已存在的同名任务
     *
     * @param taskProperties 任务配置属性对象 {@link VtTaskProperties}
     * @return Quartz 工厂定制器 {@link SchedulerFactoryBeanCustomizer}
     */
    @Bean
    @ConditionalOnMissingBean
    public SchedulerFactoryBeanCustomizer vtTaskSchedulerFactoryBeanCustomizer(VtTaskProperties taskProperties) {
        log.debug("VtTask is enabled. register SchedulerFactoryBeanCustomizer.");
        return factory -> factory.setOverwriteExistingJobs(Boolean.TRUE.equals(taskProperties.getOverwriteExistingJobs()));
    }

    /**
     * 注册任务注册器 - 启动完成后把注解与配置声明的任务注册到 Quartz
     *
     * @param scheduler          调度器对象 {@link Scheduler}
     * @param taskProperties     任务配置属性对象 {@link VtTaskProperties}
     * @param applicationContext Spring 应用上下文对象 {@link ApplicationContext}
     * @param jobListeners       任务监听器提供者
     * @return 任务注册器 {@link VtJobRegistrar}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(Scheduler.class)
    public VtJobRegistrar vtJobRegistrar(Scheduler scheduler, VtTaskProperties taskProperties,
                                         ApplicationContext applicationContext,
                                         ObjectProvider<VtJobListener> jobListeners) {
        log.debug("VtTask is enabled. register VtJobRegistrar.");
        return new VtJobRegistrar(scheduler, taskProperties, applicationContext, jobListeners.orderedStream().toList());
    }
}
