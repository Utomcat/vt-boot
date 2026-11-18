package com.ranyk.vt.boot.task.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Locale;

/**
 * CLASS_NAME: VtJobLogSchemaInitializer.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志表初始化器 - 仅在 vt.task.log.initialize-schema=always 时执行
 * @date: 2026-11-18
 */
@Slf4j
public class VtJobLogSchemaInitializer implements InitializingBean {

    /**
     * 数据源对象
     */
    private final DataSource dataSource;

    /**
     * 构造函数
     *
     * @param dataSource 数据源对象 {@link DataSource}
     */
    public VtJobLogSchemaInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 执行建表脚本
     */
    @Override
    public void afterPropertiesSet() {
        String script = "db/vt_job_log-" + resolveDriverId() + ".sql";
        ClassPathResource resource = new ClassPathResource(script);
        if (!resource.exists()) {
            log.warn("VtJob log schema script not found, skip init. script: {}", script);
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
        populator.setContinueOnError(true);
        try {
            DatabasePopulatorUtils.execute(populator, dataSource);
            log.debug("VtJob log table initialized. script: {}", script);
        } catch (Exception e) {
            log.error("VtJob log table init failed. script: {}", script, e);
        }
    }

    /**
     * 解析数据库驱动标识
     *
     * @return 驱动标识, 例如 mysql / postgresql
     */
    private String resolveDriverId() {
        try (Connection connection = dataSource.getConnection()) {
            String url = connection.getMetaData().getURL();
            String lowerUrl = url == null ? "" : url.toLowerCase(Locale.ROOT);
            return lowerUrl.contains("postgresql") ? "postgresql" : "mysql";
        } catch (Exception e) {
            log.warn("Resolve database driver failed, fallback to mysql.", e);
            return "mysql";
        }
    }
}
