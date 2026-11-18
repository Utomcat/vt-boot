package com.ranyk.vt.boot.mq.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * CLASS_NAME: RabbitMqEnabledCondition.java
 *
 * @author ranyk
 * @version V1.0
 * @description: RabbitMQ 启用条件 - 仅当显式开启 MQ 或配置了 RabbitMQ 连接信息时匹配
 * @date: 2026-11-17
 */
public class RabbitMqEnabledCondition implements Condition {

    /**
     * 判断条件是否匹配
     *
     * @param context  条件上下文对象 {@link ConditionContext}
     * @param metadata 注解元数据 {@link AnnotatedTypeMetadata}
     * @return 是否匹配
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return RabbitMqEnablement.isEnabled(context.getEnvironment());
    }
}
