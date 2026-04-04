package com.ranyk.vt.boot.mq.config;

import com.ranyk.vt.boot.mq.config.properties.MqConfigurationProperties;
import com.ranyk.vt.boot.mq.consumer.RabbitmqDefaultConsumer;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

/**
 * CLASS_NAME: RabbitMqConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: RabbitMQ 消息队列的配置类
 * @date: 2026-03-25
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "vt.boot.mq", name = "is-enable-rabbitmq", havingValue = "true")
public class RabbitMqConfiguration {

    /**
     * Mq 配置属性类
     */
    private final MqConfigurationProperties mqConfigurationProperties;

    /**
     * 构造函数，注入 {@link MqConfigurationProperties} 配置属性类
     *
     * @param mqConfigurationProperties Mq 配置属性类
     */
    @Autowired
    public RabbitMqConfiguration(MqConfigurationProperties mqConfigurationProperties) {
        this.mqConfigurationProperties = mqConfigurationProperties;
    }

    /**
     * 创建 RabbitMQ 默认 消费者
     *
     * @return RabbitMQ 默认 消费者 {@link RabbitmqDefaultConsumer} 对象
     */
    @Bean
    @SuppressWarnings("all")
    public RabbitmqDefaultConsumer rabbitmqDefaultConsumer(IOperationRecordService operationRecordService) {
        log.debug("Init RabbitMQ default consumer.");
        return new RabbitmqDefaultConsumer(operationRecordService);
    }

    /**
     * 创建 RabbitMQ 模板, 用于发送和接收消息的工具类
     *
     * @param connectionFactory 连接工厂 {@link ConnectionFactory} 对象
     * @param messageConverter  消息转换器 {@link MessageConverter} 对象
     * @return RabbitMQ 模板 {@link RabbitTemplate} 对象
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        log.debug("Init RabbitMQ template.");
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    /**
     * 创建 RabbitMQ 模板, 用于发送和接收消息的工具类
     *
     * @return RabbitMQ 消息转换器 {@link MessageConverter} 对象
     */
    @Bean
    @Primary
    @DependsOn({"simpleMessageConverter", "jackson2JsonMessageConverter"})
    public MessageConverter messageConverter(SimpleMessageConverter simpleMessageConverter, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        log.debug("Init RabbitMQ message converter. Current default message converter: {}", mqConfigurationProperties.getRabbitmqDefaultMessageConverter());
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();
        // 为 JSON 内容类型配置 Jackson 转换器
        converter.addDelegate("application/json", jackson2JsonMessageConverter);
        converter.addDelegate("text/json", jackson2JsonMessageConverter);

        // 为 Java 序列化内容类型配置 SimpleMessageConverter
        converter.addDelegate("application/x-java-serialized-object", simpleMessageConverter);
        converter.addDelegate("application/x-java-serialized-object;charset=UTF-8", simpleMessageConverter);

        // 配置默认使用的转换器
        switch (mqConfigurationProperties.getRabbitmqDefaultMessageConverter()) {
            case "simpleMessageConverter" -> converter.addDelegate("", simpleMessageConverter);
            case "jackson2JsonMessageConverter" -> converter.addDelegate("", jackson2JsonMessageConverter);
            default -> {
                log.debug("Default message converter not found, configure default message converter as simpleMessageConverter.");
                converter.addDelegate("", simpleMessageConverter);
            }
        }
        return converter;
    }

    /**
     * 创建 amqp 的消息转换器, 允许进行对指定的数据对象进行反序列化
     *
     * @return amqp 的消息转换器 {@link SimpleMessageConverter} 对象
     */
    @Bean
    public SimpleMessageConverter simpleMessageConverter() {
        log.debug("Init RabbitMQ Simple message converter. Allowed list patterns: {}", mqConfigurationProperties.getRabbitmqAllowedListPatterns());
        SimpleMessageConverter converter = new SimpleMessageConverter();
        // 允许 配置的能被允许反序列化的类
        converter.setAllowedListPatterns(mqConfigurationProperties.getRabbitmqAllowedListPatterns());
        // 或者 允许具体类 converter.addAllowedListPatterns("com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO");
        return converter;
    }

    /**
     * 创建 amqp 的消息转换器, 允许进行对指定的数据对象进行反序列化
     *
     * @return amqp 的消息转换器 {@link Jackson2JsonMessageConverter} 对象
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        log.debug("Init RabbitMQ Jackson2Json message converter. Allowed list patterns: {}", mqConfigurationProperties.getRabbitmqAllowedListPatterns());
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        defaultClassMapper.setTrustedPackages(mqConfigurationProperties.getRabbitmqAllowedListPatterns().toArray(new String[0]));
        jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);
        return jackson2JsonMessageConverter;
    }

}
