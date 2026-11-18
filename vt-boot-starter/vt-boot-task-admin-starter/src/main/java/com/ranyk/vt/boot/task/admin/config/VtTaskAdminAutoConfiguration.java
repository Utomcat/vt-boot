package com.ranyk.vt.boot.task.admin.config;

import com.ranyk.vt.boot.task.admin.controller.VtJobController;
import com.ranyk.vt.boot.task.admin.controller.VtJobLogController;
import com.ranyk.vt.boot.task.admin.service.VtJobLogService;
import com.ranyk.vt.boot.task.admin.service.VtJobService;
import com.ranyk.vt.boot.task.repository.VtJobLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: VtTaskAdminAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务管理模块自动配置类 - 仅在 Web 应用中注册管理接口
 * @date: 2026-11-18
 */
@Slf4j
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(name = "org.springframework.web.servlet.DispatcherServlet")
@ConditionalOnBooleanProperty(prefix = "vt.task.admin", name = "enabled", matchIfMissing = true)
public class VtTaskAdminAutoConfiguration {

    /**
     * 注册任务管理业务对象
     *
     * @return 任务管理业务对象 {@link VtJobService}
     */
    @Bean
    @ConditionalOnMissingBean
    public VtJobService vtTaskJobService() {
        log.debug("VtTaskAdmin is enabled. register VtJobService.");
        return new VtJobService();
    }

    /**
     * 注册任务执行日志业务对象
     *
     * @param repositoryProvider 日志仓储提供者
     * @return 任务执行日志业务对象 {@link VtJobLogService}
     */
    @Bean
    @ConditionalOnMissingBean
    public VtJobLogService vtTaskJobLogService(ObjectProvider<VtJobLogRepository> repositoryProvider) {
        log.debug("VtTaskAdmin is enabled. register VtJobLogService.");
        return new VtJobLogService(repositoryProvider);
    }

    /**
     * 注册任务管理接口
     *
     * @param jobService 任务管理业务对象 {@link VtJobService}
     * @return 任务管理接口 {@link VtJobController}
     */
    @Bean
    @ConditionalOnMissingBean
    public VtJobController vtTaskJobController(VtJobService jobService) {
        log.debug("VtTaskAdmin is enabled. register VtJobController.");
        return new VtJobController(jobService);
    }

    /**
     * 注册任务执行日志接口
     *
     * @param jobLogService 任务执行日志业务对象 {@link VtJobLogService}
     * @return 任务执行日志接口 {@link VtJobLogController}
     */
    @Bean
    @ConditionalOnMissingBean
    public VtJobLogController vtTaskJobLogController(VtJobLogService jobLogService) {
        log.debug("VtTaskAdmin is enabled. register VtJobLogController.");
        return new VtJobLogController(jobLogService);
    }
}
