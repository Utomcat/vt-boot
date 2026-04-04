package com.ranyk.vt.boot.mq.strategy;

import com.ranyk.vt.boot.base.constant.MqMessageTypeEnum;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;

/**
 * CLASS_NAME: DisruptorMqMessageHandlerStrategy.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Disruptor 消息队列处理策略接口类
 * @date: 2026-03-24
 */
public interface DisruptorMqMessageHandlerStrategy {

    /**
     * 获取消息类型
     *
     * @return 消息类型枚举对象 {@link MqMessageTypeEnum}
     */
    MqMessageTypeEnum getMessageType();

    /**
     * 执行消息处理逻辑
     *
     * @param event 消息事件对象
     */
    void execute(BaseMessageDTO event);
}
