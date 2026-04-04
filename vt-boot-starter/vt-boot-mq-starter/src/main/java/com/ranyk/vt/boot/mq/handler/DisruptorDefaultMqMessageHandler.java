package com.ranyk.vt.boot.mq.handler;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import com.ranyk.vt.boot.mq.factory.DisruptorMqMessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: DisruptorMqMessageHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Disruptor  MQ 消息队列默认的事件处理类(消息消费者)
 * @date: 2026-03-24
 */
@Slf4j
@Component
public class DisruptorDefaultMqMessageHandler implements EventHandler<BaseMessageDTO> {

    /**
     * Disruptor MQ 默认消息队列处理类工厂类
     */
    private final DisruptorMqMessageHandlerFactory disruptorMqMessageHandlerFactory;

    /**
     * 构造函数
     *
     * @param disruptorMqMessageHandlerFactory Disruptor MQ 消息队列默认的事件处理类工厂类
     */
    @Autowired
    public DisruptorDefaultMqMessageHandler(DisruptorMqMessageHandlerFactory disruptorMqMessageHandlerFactory) {
        this.disruptorMqMessageHandlerFactory = disruptorMqMessageHandlerFactory;
    }

    /**
     * Called when a publisher has published an event to the {@link RingBuffer}.  The {@link BatchEventProcessor} will
     * read messages from the {@link RingBuffer} in batches, where a batch is all of the events available to be
     * processed without having to wait for any new event to arrive.  This can be useful for event handlers that need
     * to do slower operations like I/O as they can group together the data from multiple events into a single
     * operation.  Implementations should ensure that the operation is always performed when endOfBatch is true as
     * the time between that message and the next one is indeterminate.
     *
     * @param event      published to the {@link RingBuffer}
     * @param sequence   of the event being processed
     * @param endOfBatch flag to indicate if this is the last event in a batch from the {@link RingBuffer}
     */
    @Override
    public void onEvent(BaseMessageDTO event, long sequence, boolean endOfBatch) {
        log.debug("DisruptorDefaultMqMessageHandler onEvent Method Start Invoked.");
        log.debug("消息队列中的消息消费中, 当前传入的消息事件序列号为: {}, 消息对象为: {}, 当前处理的消息是否为消息队列的最后一个消息: {}", sequence, event, endOfBatch);
        disruptorMqMessageHandlerFactory.execute(event);
        log.debug("DisruptorDefaultMqMessageHandler onEvent Method End Invoked.");
    }
}
