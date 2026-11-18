package com.ranyk.vt.boot.mq.config.condition;

import org.springframework.core.env.Environment;

/**
 * CLASS_NAME: RabbitMqEnablement.java
 *
 * @author ranyk
 * @version V1.0
 * @description: RabbitMQ 启用状态判定工具类
 * <p>
 * 判定规则: 仅以显式开关 {@code vt.boot.mq.is-enable-rabbitmq=true} 为准。
 * 未开启开关时, 即使存在 {@code spring.rabbitmq.*} 连接信息也不会装配 RabbitMQ 相关 Bean;
 * 开启开关但未配置连接信息时, 由 Spring Boot 使用默认的本地连接信息(localhost:5672) 进行连接。
 * </p>
 * @date: 2026-11-17
 */
public final class RabbitMqEnablement {

    /**
     * RabbitMQ 显式启用配置项
     */
    public static final String ENABLE_PROPERTY = "vt.boot.mq.is-enable-rabbitmq";

    /**
     * 私有构造方法 - 工具类不允许实例化
     */
    private RabbitMqEnablement() {
    }

    /**
     * 判断当前环境是否启用 RabbitMQ
     *
     * @param environment 环境对象 {@link Environment}
     * @return 是否启用 RabbitMQ
     */
    public static boolean isEnabled(Environment environment) {
        if (environment == null) {
            return false;
        }
        return Boolean.TRUE.equals(environment.getProperty(ENABLE_PROPERTY, Boolean.class, Boolean.FALSE));
    }
}
