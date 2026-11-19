package com.ranyk.vt.boot.task.config;

import com.ranyk.vt.boot.task.properties.VtTaskStoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Locale;

/**
 * CLASS_NAME: VtQuartzSchemaInitializer.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Quartz 存储表初始化器 - 仅在 vt.task.store.initialize-schema=always 时执行
 * <p>
 * 使用框架内置的幂等建表脚本(CREATE TABLE IF NOT EXISTS), 可重复执行且不会清空已持久化的任务数据;
 * 与 Quartz 官方脚本(以 DROP TABLE IF EXISTS 开头)有本质区别, 不可混用。
 * </p>
 * @date: 2026-11-20
 */
@Slf4j
public class VtQuartzSchemaInitializer implements InitializingBean {

    /**
     * classpath 前缀
     */
    private static final String CLASSPATH_PREFIX = "classpath:";

    /**
     * 脚本中的数据库类型占位符
     */
    private static final String PLATFORM_PLACEHOLDER = "@@platform@@";

    /**
     * 数据源对象
     */
    private final DataSource dataSource;

    /**
     * 存储配置属性对象
     */
    private final VtTaskStoreProperties storeProperties;

    /**
     * 构造函数
     *
     * @param dataSource      数据源对象 {@link DataSource}
     * @param storeProperties 存储配置属性对象 {@link VtTaskStoreProperties}
     */
    public VtQuartzSchemaInitializer(DataSource dataSource, VtTaskStoreProperties storeProperties) {
        this.dataSource = dataSource;
        this.storeProperties = storeProperties;
    }

    /**
     * 执行建表脚本
     */
    @Override
    public void afterPropertiesSet() {
        String platform = resolvePlatform();
        String script = resolveScript(platform);
        if (!StringUtils.hasText(script)) {
            log.warn("VtTask store schema init skipped. platform: {}, set vt.task.store.initialize-schema=never "
                    + "and create QRTZ_* tables manually if the database is not supported.", platform);
            return;
        }
        ClassPathResource resource = new ClassPathResource(script);
        if (!resource.exists()) {
            log.warn("VtTask store schema script not found, skip init. script: {}", script);
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
        populator.setCommentPrefixes("--", "#");
        populator.setContinueOnError(true);
        try {
            DatabasePopulatorUtils.execute(populator, dataSource);
            log.debug("VtTask store schema initialized. script: {}", script);
        } catch (Exception e) {
            log.error("VtTask store schema init failed. script: {}", script, e);
        }
    }

    /**
     * 解析数据库类型标识
     *
     * @return 数据库类型标识, 例如 mysql_innodb / postgres / h2; 无法识别时返回 null
     */
    private String resolvePlatform() {
        String url;
        try (Connection connection = dataSource.getConnection()) {
            url = connection.getMetaData().getURL();
        } catch (Exception e) {
            log.warn("VtTask store resolve database url failed, schema init skipped.", e);
            return null;
        }
        String lowerUrl = url == null ? "" : url.toLowerCase(Locale.ROOT);
        if (lowerUrl.contains("postgresql")) {
            return "postgres";
        }
        if (lowerUrl.contains("mariadb") || lowerUrl.contains("mysql")) {
            return "mysql_innodb";
        }
        if (lowerUrl.contains(":h2:")) {
            return "h2";
        }
        return null;
    }

    /**
     * 解析建表脚本位置 - 替换占位符并去掉 classpath 前缀
     *
     * @param platform 数据库类型标识
     * @return 脚本位置, 无法解析时返回 null
     */
    private String resolveScript(String platform) {
        if (!StringUtils.hasText(platform)) {
            return null;
        }
        String location = storeProperties.getSchema();
        if (!StringUtils.hasText(location)) {
            return null;
        }
        String script = location.startsWith(CLASSPATH_PREFIX)
                ? location.substring(CLASSPATH_PREFIX.length()) : location;
        return script.replace(PLATFORM_PLACEHOLDER, platform);
    }
}
