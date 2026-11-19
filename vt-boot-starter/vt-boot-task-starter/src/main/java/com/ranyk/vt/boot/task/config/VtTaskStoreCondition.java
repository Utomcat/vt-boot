package com.ranyk.vt.boot.task.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * CLASS_NAME: VtTaskStoreCondition.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务 JDBC 存储生效条件 - 框架默认使用 JDBC, 使用方显式声明存储类型时以其配置为准
 * <p>
 * {@code spring.quartz.job-store-type} 显式配置为 memory 时存储自动配置整体后退;
 * 未配置或配置为 jdbc 时由框架装配数据源, 并保证建表脚本先于 Quartz 初始化执行。
 * </p>
 * @date: 2026-11-20
 */
public class VtTaskStoreCondition implements Condition {

    /**
     * 默认存储类型
     */
    private static final String DEFAULT_STORE_TYPE = "jdbc";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String storeType = environment.getProperty("vt.task.store.type", DEFAULT_STORE_TYPE);
        if (!DEFAULT_STORE_TYPE.equalsIgnoreCase(storeType)) {
            return false;
        }
        String quartzStoreType = environment.getProperty("spring.quartz.job-store-type");
        return quartzStoreType == null || DEFAULT_STORE_TYPE.equalsIgnoreCase(quartzStoreType);
    }
}
