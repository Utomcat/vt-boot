package com.ranyk.vt.boot.mq.env;

import com.ranyk.vt.boot.mq.config.condition.RabbitMqEnablement;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * CLASS_NAME: RabbitMqAutoConfigurationImportFilter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: RabbitMQ 自动配置过滤器
 * <p>
 * 避免仅引入 vt-boot-mq-starter(如通过 vt-boot-log-starter 间接引入) 而未配置 MQ 的工程,
 * 被 Spring Boot 的 {@code RabbitAutoConfiguration} 默认装配出连接工厂与健康检查,
 * 从而在启动时不断尝试连接 localhost:5672 并打印 "Rabbit health check failed" 告警。
 * </p>
 * <p>
 * 仅当显式开启 MQ({@code vt.boot.mq.is-enable-rabbitmq=true}) 时, 才放行 RabbitMQ 自动配置;
 * 未开启开关时, 即使存在 {@code spring.rabbitmq.*} 连接信息也不会装配相关 Bean。
 * </p>
 * @date: 2026-11-17
 */
public class RabbitMqAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware, Ordered {

    /**
     * Spring Boot 的 RabbitMQ 自动配置类全限定名
     */
    private static final String RABBIT_AUTO_CONFIGURATION =
            "org.springframework.boot.amqp.autoconfigure.RabbitAutoConfiguration";

    /**
     * 环境对象
     */
    private Environment environment;

    /**
     * 设置环境对象
     *
     * @param environment 环境对象 {@link Environment}
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 过滤自动配置类 - 未被启用的 RabbitMQ 自动配置将被剔除
     *
     * @param autoConfigurationClasses  待加载的自动配置类全限定名数组
     * @param autoConfigurationMetadata 自动配置元数据
     * @return 每个自动配置类是否放行
     */
    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] matches = new boolean[autoConfigurationClasses.length];
        boolean rabbitMqEnabled = RabbitMqEnablement.isEnabled(environment);
        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            matches[i] = rabbitMqEnabled || !RABBIT_AUTO_CONFIGURATION.equals(autoConfigurationClasses[i]);
        }
        return matches;
    }

    /**
     * 过滤器执行顺序 - 置于高优先级, 在类路径条件过滤之前完成剔除
     *
     * @return 顺序值
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
