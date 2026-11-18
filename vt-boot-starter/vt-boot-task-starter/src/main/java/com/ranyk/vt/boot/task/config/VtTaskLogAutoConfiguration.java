package com.ranyk.vt.boot.task.config;

import com.ranyk.vt.boot.task.handle.VtJobListener;
import com.ranyk.vt.boot.task.properties.VtTaskLogProperties;
import com.ranyk.vt.boot.task.repository.VtJobLogRepository;
import com.ranyk.vt.boot.task.repository.impl.JdbcVtJobLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * CLASS_NAME: VtTaskLogAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志自动配置类 - 仅在存在 spring-jdbc 与 JdbcTemplate 时生效
 * <p>
 * 单独成类是为了做到"没有数据源的工程引入 task-starter 也不会因为缺少 spring-jdbc 而启动失败"。
 * </p>
 * @date: 2026-11-18
 */
@Slf4j
@AutoConfiguration(after = VtTaskAutoConfiguration.class,
        afterName = "org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration")
@ConditionalOnClass(JdbcTemplate.class)
@ConditionalOnBean(JdbcTemplate.class)
@EnableConfigurationProperties(VtTaskLogProperties.class)
@ConditionalOnBooleanProperty(prefix = "vt.task.log", name = "enabled", matchIfMissing = true)
public class VtTaskLogAutoConfiguration {

    /**
     * 注册任务执行日志仓储
     *
     * @param jdbcTemplate  JDBC 模板对象 {@link JdbcTemplate}
     * @param logProperties 日志配置属性对象 {@link VtTaskLogProperties}
     * @return 任务执行日志仓储对象 {@link VtJobLogRepository}
     */
    @Bean
    @ConditionalOnMissingBean
    public VtJobLogRepository vtJobLogRepository(JdbcTemplate jdbcTemplate, VtTaskLogProperties logProperties) {
        log.debug("VtTask log is enabled. register JdbcVtJobLogRepository.");
        return new JdbcVtJobLogRepository(jdbcTemplate, logProperties);
    }

    /**
     * 注册任务监听器 - 记录任务执行日志
     *
     * @param jobLogRepository 任务执行日志仓储对象 {@link VtJobLogRepository}
     * @return 任务监听器 {@link VtJobListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public VtJobListener vtJobListener(VtJobLogRepository jobLogRepository) {
        log.debug("VtTask log is enabled. register VtJobListener.");
        return new VtJobListener(jobLogRepository);
    }

    /**
     * 注册任务执行日志表初始化器 - 仅在 initialize-schema=always 时执行建表脚本
     *
     * @param dataSource 数据源对象 {@link DataSource}
     * @return 初始化器 {@link VtJobLogSchemaInitializer}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnProperty(prefix = "vt.task.log", name = "initialize-schema", havingValue = "always")
    public VtJobLogSchemaInitializer vtJobLogSchemaInitializer(DataSource dataSource) {
        log.debug("VtTask log schema init is enabled.");
        return new VtJobLogSchemaInitializer(dataSource);
    }
}
