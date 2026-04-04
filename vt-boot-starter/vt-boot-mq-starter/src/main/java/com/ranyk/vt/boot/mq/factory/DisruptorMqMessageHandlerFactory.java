package com.ranyk.vt.boot.mq.factory;

import com.ranyk.vt.boot.base.constant.MessageKeyEnum;
import com.ranyk.vt.boot.base.constant.MqMessageTypeEnum;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import com.ranyk.vt.boot.mq.strategy.DisruptorMqMessageHandlerStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CLASS_NAME: DisruptorMqMessageHandlerFactory.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Disruptor 消息队列处理对象
 * @date: 2026-03-24
 */
@Slf4j
@Component
public class DisruptorMqMessageHandlerFactory {

    /**
     * 消息处理策略对象缓存 Map 集合
     */
    private static final ConcurrentHashMap<MqMessageTypeEnum, DisruptorMqMessageHandlerStrategy> HANDLER_STRATEGY_MAP = new ConcurrentHashMap<>();

    /**
     * 构造函数
     *
     * @param handlerStrategies 消息处理策略对象列表
     */
    @Autowired
    public DisruptorMqMessageHandlerFactory(List<DisruptorMqMessageHandlerStrategy> handlerStrategies) {
        handlerStrategies.forEach(handlerStrategy -> HANDLER_STRATEGY_MAP.put(handlerStrategy.getMessageType(), handlerStrategy));
    }

    /**
     * 执行消息处理逻辑
     *
     * @param event 待处理的消息事件对象
     */
    public void execute(BaseMessageDTO event) {
        log.debug("Start Execute Handler Message Logic, Current Message Event Object is: {}", event);
        // 从 消息事件对象中获取当前的 消息类型
        MqMessageTypeEnum messageType = MqMessageTypeEnum.valueOf(String.valueOf(event.getHeader().get(MessageKeyEnum.MESSAGE_HEADER_TYPE_KEY.getValue())));
        // 获取对应的消息类型处理策略对象
        DisruptorMqMessageHandlerStrategy disruptorMqMessageHandlerStrategy = HANDLER_STRATEGY_MAP.get(messageType);
        // 判断消息处理策略对象是否为空
        if (Objects.isNull(disruptorMqMessageHandlerStrategy)){
            // 消息处理策略对象是空, 不进行逻辑业务处理, 打印错误信息
            log.error("未找到对应的消息处理策略对象, 不进行相关的逻辑业务处理, 本次的消息类型为: {}", messageType);
        } else {
            // 存在对应的消息处理策略对象, 执行对应的消息处理逻辑
            disruptorMqMessageHandlerStrategy.execute(event);
        }
        log.debug("End Execute Handler Message Logic.");
    }

}
