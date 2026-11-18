package com.ranyk.vt.boot.mq.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS_NAME: MqConfigurationProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: MQ 消息队列配置属性类
 * @date: 2026-03-24
 */
@Data
@ConfigurationProperties(prefix = "vt.boot.mq")
public class MqConfigurationProperties {

    /**
     * 是否开启 disruptor 消息队列 - 框架默认的本地异步队列, 缺省开启
     */
    private Boolean isEnableDisruptor = Boolean.TRUE;
    /**
     * 是否开启 rabbitmq 队列 - 默认关闭, 需要使用的工程必须显式置为 true
     */
    private Boolean isEnableRabbitMQ = Boolean.FALSE;
    /**
     * rabbitmq 允许进行反序列化的类列表
     */
    private List<String> rabbitmqAllowedListPatterns = new ArrayList<>(List.of(
            "java.util.*",
            "java.lang.*",
            "sun.reflect.annotation.*",
            "com.ranyk.vt.boot.base.domain.*",
            "com.ranyk.vt.boot.base.constant.*",
            "com.ranyk.vt.boot.mq.domain.dto.*",
            "com.ranyk.vt.boot.log.annotations.*"
    ));
    /**
     * Rabbitmq 默认的消息转换器, 当前系统内存在的消息转换器为 simpleMessageConverter 和 jacksonJsonMessageConverter 两种, 通过如下配置属性配置默认使用那个转换器
     */
    private String rabbitmqDefaultMessageConverter = "simpleMessageConverter";
   }
