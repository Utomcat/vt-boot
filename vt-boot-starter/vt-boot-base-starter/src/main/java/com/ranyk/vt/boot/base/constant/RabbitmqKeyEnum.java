package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: RabbitmqKeyEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Rabbitmq 配置枚举
 * @date: 2026-03-25
 */
@Getter
public enum RabbitmqKeyEnum {

    /**
     * RabbitMQ 默认 队列名称
     */
    DEFAULT_RABBITMQ_QUEUE("RabbitMQ 默认 队列名称", "default_queue"),
    /**
     * RabbitMQ 默认 交换机名称
     */
    DEFAULT_RABBITMQ_EXCHANGE("RabbitMQ 默认 交换机名称", "default_exchange"),
    /**
     * RabbitMQ 默认  路由键
     */
    DEFAULT_RABBITMQ_ROUTING_KEY("RabbitMQ 默认 路由键", "default_routing_key"),
    ;


    /**
     * 描述
     */
    private final String desc;
    /**
     * 值
     */
    private final String value;

    /**
     * 构造方法
     *
     * @param desc  描述
     * @param value 值
     */
    RabbitmqKeyEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
