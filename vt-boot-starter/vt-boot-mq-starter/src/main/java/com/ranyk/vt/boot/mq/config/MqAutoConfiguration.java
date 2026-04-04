package com.ranyk.vt.boot.mq.config;

import com.ranyk.vt.boot.mq.config.properties.MqConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * CLASS_NAME: MqAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: MQ 消息队列自动配置类
 * @date: 2026-03-24
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(MqConfigurationProperties.class)
@Import(value = {DisruptorAutoConfiguration.class, RabbitMqConfiguration.class})
public class MqAutoConfiguration {


}
