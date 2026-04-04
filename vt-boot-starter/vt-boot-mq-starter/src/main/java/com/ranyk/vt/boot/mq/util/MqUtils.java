package com.ranyk.vt.boot.mq.util;

import cn.hutool.extra.spring.SpringUtil;
import com.ranyk.vt.boot.base.constant.MessageKeyEnum;
import com.ranyk.vt.boot.base.constant.MqTypeEnum;
import com.ranyk.vt.boot.base.exception.MqException;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import com.ranyk.vt.boot.mq.publisher.DisruptorDefaultMqMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;

/**
 * CLASS_NAME: MqUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 消息队列工具类, 主要用于在其他工程中使用消息队列的消息推送方法
 * @date: 2026-03-24
 */
@Slf4j
@SuppressWarnings("unused")
public class MqUtils {

    /**
     * Disruptor 消息队列默认的事件发布者
     */
    private static volatile DisruptorDefaultMqMessagePublisher disruptorDefaultMqMessagePublisher;
    /**
     * RabbitMQ 消息队列默认的事件发布者
     */
    private static volatile RabbitTemplate rabbitTemplate;
    /**
     * 默认的消息队列类型, 默认为 Disruptor 消息队列
     */
    private static final MqTypeEnum DEFAULT_MQ_TYPE = MqTypeEnum.DISRUPTOR;

    /**
     * 获取 Disruptor 默认的消息发布者
     *
     * @return Disruptor 默认的消息发布者 {@link DisruptorDefaultMqMessagePublisher}
     */
    private static DisruptorDefaultMqMessagePublisher getDisruptorDefaultMqMessagePublisher() {
        if (disruptorDefaultMqMessagePublisher == null) {
            synchronized (MqUtils.class) {
                if (disruptorDefaultMqMessagePublisher == null) {
                    try {
                        disruptorDefaultMqMessagePublisher = SpringUtil.getBean(DisruptorDefaultMqMessagePublisher.class);
                    } catch (Exception e) {
                        log.error("在获取 Disruptor 消息队列默认的事件发布者时失败, 异常信息 => ", e);
                        throw new MqException("获取 Disruptor 消息队列默认的事件发布者时发生异常", e);
                    }
                }
            }
        }
        return disruptorDefaultMqMessagePublisher;
    }

    /**
     * 获取 RabbitMQ 默认的消息发布者
     *
     * @return RabbitMQ 默认的消息发布者 {@link RabbitTemplate}
     */
    private static RabbitTemplate getRabbitTemplate() {
        if (rabbitTemplate == null) {
            synchronized (MqUtils.class) {
                if (rabbitTemplate == null) {
                    try {
                        rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);
                    } catch (Exception e) {
                        log.error("在获取 RabbitMQ 默认的消息发布者时失败, 错误信息 => ", e);
                        throw new MqException("获取 RabbitMQ 默认的消息发布者时发生异常", e);
                    }
                }
            }
        }
        return rabbitTemplate;
    }

    /**
     * 发送消息 - 使用默认的消息队列类型(Disruptor) {@link DisruptorDefaultMqMessagePublisher}
     *
     * @param header 消息头
     * @param body   消息体
     */
    public static void publish(Map<String, Object> header, Map<String, Object> body) {
        publish(header, body, DEFAULT_MQ_TYPE);
    }

    /**
     * 发送消息 - 使用指定的消息队列类型 {@link MqTypeEnum}
     *
     * @param header 消息头
     * @param body   消息体
     * @param mqTypeEnum 消息队列类型 {@link MqTypeEnum}
     */
    public static void publish(Map<String, Object> header, Map<String, Object> body, MqTypeEnum mqTypeEnum) {
        switch (mqTypeEnum) {
            case DISRUPTOR -> disruptorPublish(header, body);
            case RABBITMQ -> rabbitMQPublish(header, body);
            default -> {
                log.error("不支持的消息队列类型, 请检查消息队列类型是否正确");
                throw new MqException("不支持的消息队列类型, 请检查消息队列类型是否正确");
            }
        }
    }

    /**
     * 发送消息 - 使用 Disruptor 默认的消息发布者 {@link DisruptorDefaultMqMessagePublisher}
     *
     * @param header 消息头
     * @param body   消息体
     */
    private static void disruptorPublish(Map<String, Object> header, Map<String, Object> body) {
        getDisruptorDefaultMqMessagePublisher().publish(header, body);
    }

    /**
     * 发送消息 - 使用 RabbitMQ 默认的消息发布者 {@link RabbitTemplate}
     *
     * @param header 消息头
     * @param body   消息体
     */
    private static void rabbitMQPublish(Map<String, Object> header, Map<String, Object> body) {
        // 从消息头中获取 RabbitMQ 交换机名称
        String exchange = header.get(MessageKeyEnum.MESSAGE_HEADER_RABBITMQ_EXCHANGE_KEY.getValue()).toString();
        // 从消息头中获取 RabbitMQ 路由键名称
        String routingKey = header.get(MessageKeyEnum.MESSAGE_HEADER_RABBITMQ_ROUTING_KEY.getValue()).toString();
        // 通过 RabbitMQ 默认的消息发布者 发送消息到指定的 RabbitMQ 交换机 和 路由键 中
        getRabbitTemplate().convertAndSend(exchange, routingKey, BaseMessageDTO.builder().header(header).body(body).build());
    }

}
