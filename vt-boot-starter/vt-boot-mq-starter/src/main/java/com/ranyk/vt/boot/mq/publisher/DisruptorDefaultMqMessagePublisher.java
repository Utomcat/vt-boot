package com.ranyk.vt.boot.mq.publisher;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * CLASS_NAME: DisruptorDefaultMqMessagePublisher.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Disruptor  MQ 消息队列默认的事件发布类(消息生产者)
 * @date: 2026-03-24
 */
@Component
public class DisruptorDefaultMqMessagePublisher {

    /**
     * 创建 Disruptor 默认消息队列事件发布者
     */
    private final RingBuffer<BaseMessageDTO> ringBuffer;
    /**
     * 预定义 Translator 消息翻译对象对象
     */
    private final EventTranslatorTwoArg<BaseMessageDTO, Map<String, Object>, Map<String, Object>> TRANSLATOR = (event, sequence, header, body) -> {
        event.setHeader(header);
        event.setBody(body);
    };

    /**
     * 构造函数
     *
     * @param ringBuffer Disruptor 默认消息队列事件发布者
     */
    @Autowired
    public DisruptorDefaultMqMessagePublisher(RingBuffer<BaseMessageDTO> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 发布消息
     *
     * @param header 消息头
     * @param body   消息体
     */
    public void publish(Map<String, Object> header, Map<String, Object> body) {
        ringBuffer.publishEvent(TRANSLATOR, header, body);
    }

}
