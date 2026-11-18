package com.ranyk.vt.boot.mq.config;

import com.ranyk.vt.boot.mq.config.condition.RabbitMqEnabledCondition;
import com.ranyk.vt.boot.mq.config.properties.MqConfigurationProperties;
import com.ranyk.vt.boot.mq.consumer.RabbitmqDefaultConsumer;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

/**
 * CLASS_NAME: RabbitMqConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: RabbitMQ 消息队列的配置类
 * <p>
 * 注: Spring Boot 4 起默认使用 Jackson 3, 因此 JSON 消息转换器由 Jackson 2 版本的转换器
 * 迁移为 {@link JacksonJsonMessageConverter}, 否则运行时会出现 Jackson 2 相关类缺失的异常。
 * </p>
 * <p>
 * 仅当显式开启 MQ({@code vt.boot.mq.is-enable-rabbitmq=true}) 时才会装配,
 * 未开启 MQ 的工程不会创建 RabbitMQ 相关 Bean。
 * 开启开关但未配置连接信息时, 由 Spring Boot 使用默认的本地连接信息(localhost:5672) 进行连接。
 * </p>
 * @date: 2026-03-25
 */
@Slf4j
@AutoConfiguration
@Conditional(RabbitMqEnabledCondition.class)
@RequiredArgsConstructor
public class RabbitMqConfiguration {

    /**
     * Mq 配置属性类
     */
    private final MqConfigurationProperties mqConfigurationProperties;

    /**
     * 创建 RabbitMQ 默认 消费者 - 仅在存在操作记录服务时注册
     *
     * @param operationRecordService 操作记录业务逻辑接口对象
     * @return RabbitMQ 默认 消费者 {@link RabbitmqDefaultConsumer} 对象
     */
    @Bean
    @ConditionalOnBean(IOperationRecordService.class)
    @ConditionalOnMissingBean(RabbitmqDefaultConsumer.class)
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
    @ConditionalOnMissingBean(RabbitTemplate.class)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        log.debug("Init RabbitMQ template.");
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    /**
     * 创建 RabbitMQ 消息转换器, 根据配置项决定默认使用的转换器
     *
     * @param simpleMessageConverter     简单消息转换器 {@link SimpleMessageConverter} 对象
     * @param jacksonJsonMessageConverter Jackson JSON 消息转换器 {@link JacksonJsonMessageConverter} 对象
     * @return RabbitMQ 消息转换器 {@link MessageConverter} 对象
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "messageConverter")
    @DependsOn({"simpleMessageConverter", "jacksonJsonMessageConverter"})
    public MessageConverter messageConverter(SimpleMessageConverter simpleMessageConverter,
                                             JacksonJsonMessageConverter jacksonJsonMessageConverter) {
        log.debug("Init RabbitMQ message converter. Current default message converter: {}", mqConfigurationProperties.getRabbitmqDefaultMessageConverter());
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();
        // 为 JSON 内容类型配置 Jackson 转换器
        converter.addDelegate("application/json", jacksonJsonMessageConverter);
        converter.addDelegate("text/json", jacksonJsonMessageConverter);

        // 为 Java 序列化内容类型配置 SimpleMessageConverter
        converter.addDelegate("application/x-java-serialized-object", simpleMessageConverter);
        converter.addDelegate("application/x-java-serialized-object;charset=UTF-8", simpleMessageConverter);

        // 配置默认使用的转换器
        switch (mqConfigurationProperties.getRabbitmqDefaultMessageConverter()) {
            case "simpleMessageConverter" -> converter.addDelegate("", simpleMessageConverter);
            case "jacksonJsonMessageConverter" -> converter.addDelegate("", jacksonJsonMessageConverter);
            default -> {
                log.debug("Default message converter not found, configure default message converter as simpleMessageConverter.");
                converter.addDelegate("", simpleMessageConverter);
            }
        }
        return converter;
    }

    /**
     * 创建 amqp 的简单消息转换器, 允许进行对指定的数据对象进行反序列化
     *
     * @return amqp 的消息转换器 {@link SimpleMessageConverter} 对象
     */
    @Bean
    @ConditionalOnMissingBean(SimpleMessageConverter.class)
    public SimpleMessageConverter simpleMessageConverter() {
        log.debug("Init RabbitMQ Simple message converter. Allowed list patterns: {}", mqConfigurationProperties.getRabbitmqAllowedListPatterns());
        SimpleMessageConverter converter = new SimpleMessageConverter();
        // 允许 配置的能被允许反序列化的类
        converter.setAllowedListPatterns(mqConfigurationProperties.getRabbitmqAllowedListPatterns());
        // 或者 允许具体类 converter.addAllowedListPatterns("com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO");
        return converter;
    }

    /**
     * 创建 amqp 的 Jackson JSON 消息转换器, 允许进行对指定的数据对象进行反序列化
     * <p>
     * Spring Boot 4 起默认使用 Jackson 3, 此处使用基于 Jackson 3 的 {@link JacksonJsonMessageConverter}。
     * </p>
     *
     * @return amqp 的消息转换器 {@link JacksonJsonMessageConverter} 对象
     */
    @Bean
    @ConditionalOnMissingBean(JacksonJsonMessageConverter.class)
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        log.debug("Init RabbitMQ Jackson JSON message converter. Allowed list patterns: {}", mqConfigurationProperties.getRabbitmqAllowedListPatterns());
        JacksonJsonMessageConverter jacksonJsonMessageConverter = new JacksonJsonMessageConverter();
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        defaultClassMapper.setTrustedPackages(mqConfigurationProperties.getRabbitmqAllowedListPatterns().toArray(new String[0]));
        jacksonJsonMessageConverter.setClassMapper(defaultClassMapper);
        return jacksonJsonMessageConverter;
    }

}