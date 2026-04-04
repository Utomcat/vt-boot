package com.ranyk.vt.boot.base.constant;

/**
 * CLASS_NAME: MqTypeEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: MQ 消息队列类型枚举类
 * @date: 2026-03-24
 */
public enum MqTypeEnum {
    /**
     * Disruptor 队列
     */
    DISRUPTOR,
    /**
     * Kafka 队列
     */
    KAFKA,
    /**
     * RocketMQ 队列
     */
    ROCKETMQ,
    /**
     * ActiveMQ 队列
     */
    ACTIVEMQ,
    /**
     * RabbitMQ 队列
     */
    RABBITMQ,
    /**
     * Pulsar 队列
     */
    PULSAR,
    /**
     * 其他 队列
     */
    OTHER
}