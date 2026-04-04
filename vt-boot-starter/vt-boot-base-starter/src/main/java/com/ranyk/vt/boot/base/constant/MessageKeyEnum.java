package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: MessageKeyEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: MQ消息 KEY 枚举类
 * @date: 2026-03-24
 */
@Getter
public enum MessageKeyEnum {
    /**
     * MQ 消息头 KEY 枚举
     */
    MESSAGE_HEADER_KEY("消息头 KEY", "HEADER"),
    /**
     * MQ 消息类型 KEY 枚举, 该数据存放于消息头中, 该数据的值参见 {@link MqMessageTypeEnum}
     */
    MESSAGE_HEADER_TYPE_KEY("消息类型 KEY", "TYPE"),
    /**
     * RabbitMQ 交换机 KEY 枚举, 该数据存放于消息头中
     */
    MESSAGE_HEADER_RABBITMQ_EXCHANGE_KEY("RabbitMQ 交换机 KEY", "RABBITMQ_EXCHANGE"),
    /**
     * RabbitMQ 路由 KEY 枚举, 该数据存放于消息头中
     */
    MESSAGE_HEADER_RABBITMQ_ROUTING_KEY("RabbitMQ 路由 KEY", "RABBITMQ_ROUTING_KEY"),
    /**
     * MQ 消息体 KEY 枚举
     */
    MESSAGE_BODY_KEY("消息体 KEY", "BODY"),
    /**
     * MQ 消息数据 KEY 枚举, 该数据存放于消息体中
     */
    MESSAGE_BODY_DATA_KEY("消息数据 KEY", "DATA"),
    /**
     * MQ 消息数据 - 日志类型消息的参数 KEY 枚举
     */
    MESSAGE_BODY_DATA_LOG_PARAM_KEY("消息数据 - 日志类型消息的参数 KEY", "PARAM"),
    /**
     * MQ 消息数据 - 日志类型消息的操作记录 KEY 枚举
     */
    MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_KEY("消息数据 - 日志类型消息的操作记录 KEY", "OPERATION_RECORD"),
    /**
     * MQ 消息数据 - 日志类型消息的操作记录名称 KEY 枚举
     */
    MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_NAME_KEY("消息数据 - 日志类型消息的操作记录名称 KEY", "OPERATION_RECORD_NAME"),
    /**
     * MQ 消息数据 - 日志类型消息的操作记录类型 KEY 枚举
     */
    MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_TYPE_KEY("消息数据 - 日志类型消息的操作记录类型 KEY", "OPERATION_RECORD_TYPE"),
    /**
     * MQ 消息数据 - 日志类型消息的操作记录参数 KEY 枚举
     */
    MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_PARAM_KEY("消息数据 - 日志类型消息的操作记录参数 KEY", "OPERATION_RECORD_PARAM"),
    /**
     * MQ 消息数据 - 日志类型消息的操作记录操作员 KEY 枚举
     */
    MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_OPERATOR_KEY("消息数据 - 日志类型消息的操作记录操作员 KEY", "OPERATION_RECORD_OPERATOR"),
    /**
     * MQ 消息数据 - 日志类型消息的操作记录模块 KEY 枚举
     */
    MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_MODULE_KEY("消息数据 - 日志类型消息的操作记录模块 KEY", "OPERATION_RECORD_MODULE"),
    ;

    /**
     * MQ 消息 KEY 描述
     */
    private final String desc;
    /**
     * MQ 消息 KEY 值
     */
    private final String value;

    /**
     * 构造函数
     *
     * @param desc  MQ 描述
     * @param value MQ 值
     */
    MessageKeyEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
