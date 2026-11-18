package com.ranyk.vt.boot.task.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CLASS_NAME: VtTaskLogProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志配置属性类
 * <p>
 * 仅在容器中存在 JdbcTemplate 时生效; 未引入数据源的工程不会写入执行日志。
 * 建表 SQL 见 starter 内 db/vt_job_log-*.sql, 默认不自动建表, 需要时把 initialize-schema 置为 always。
 * </p>
 * @date: 2026-11-18
 */
@Data
@ConfigurationProperties(prefix = "vt.task.log")
public class VtTaskLogProperties {

    /**
     * 是否启用任务执行日志, 默认启用
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * 执行日志表名, 默认 vt_job_log
     */
    private String tableName = "vt_job_log";

    /**
     * 是否自动建表, 默认 never; 可选 always / never
     */
    private String initializeSchema = "never";

    /**
     * 异常信息最大保存长度, 默认 4000, 超出部分截断
     */
    private Integer maxErrorLength = 4000;
}
