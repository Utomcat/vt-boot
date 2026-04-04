package com.ranyk.vt.boot.mq.strategy.impl;

import com.ranyk.vt.boot.base.constant.MqMessageTypeEnum;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import com.ranyk.vt.boot.mq.strategy.DisruptorMqMessageHandlerStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: SmsDisruptorMqMessageHandlerStrategyImpl.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Disruptor 短信消息队列处理策略实现类
 * @date: 2026-03-24
 */
@Slf4j
@Component
public class SmsDisruptorMqMessageHandlerStrategyImpl implements DisruptorMqMessageHandlerStrategy {
    /**
     * 获取消息类型
     *
     * @return 消息类型枚举对象 {@link MqMessageTypeEnum}
     */
    @Override
    public MqMessageTypeEnum getMessageType() {
        return MqMessageTypeEnum.SMS;
    }

    /**
     * 执行消息处理逻辑
     *
     * @param event 消息事件对象
     */
    @Override
    public void execute(BaseMessageDTO event) {
        log.debug("功能开发中, 敬请期待!");
    }
}
