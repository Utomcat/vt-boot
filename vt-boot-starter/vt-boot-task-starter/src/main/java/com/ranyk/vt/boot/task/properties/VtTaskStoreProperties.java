package com.ranyk.vt.boot.task.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CLASS_NAME: VtTaskStoreProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务存储配置属性类 - 控制 Quartz 任务是否持久化到数据库及建表脚本
 * <p>
 * 仅在容器中存在数据源时生效; 未引入数据源的工程自动退回 Quartz 默认的内存存储。
 * </p>
 * @date: 2026-11-20
 */
@Data
@ConfigurationProperties(prefix = "vt.task.store")
public class VtTaskStoreProperties {

    /**
     * 存储类型, 默认 jdbc(任务与触发器持久化到数据库, 重启不丢失); 配置为 memory 表示使用 Quartz 内存存储
     */
    private String type = "jdbc";

    /**
     * 是否自动执行建表脚本, 默认 always; 可选 always / never
     * <p>
     * 脚本为幂等脚本(表已存在即跳过), 不会清空已持久化的任务数据。
     * </p>
     */
    private String initializeSchema = "always";

    /**
     * 建表脚本位置, 默认 db/quartz-@@platform@@.sql, 其中 @@platform@@ 按数据库类型替换
     * <p>
     * 框架内置 mysql_innodb / postgres / h2 三套脚本, 其它数据库请自行提供或关闭自动建表。
     * </p>
     */
    private String schema = "db/quartz-@@platform@@.sql";
}
