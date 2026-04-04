package com.ranyk.vt.boot.mq.strategy.impl;

import com.ranyk.vt.boot.base.constant.MessageKeyEnum;
import com.ranyk.vt.boot.base.constant.MqMessageTypeEnum;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import com.ranyk.vt.boot.mq.strategy.DisruptorMqMessageHandlerStrategy;
import com.ranyk.vt.boot.mq.util.MessageUtils;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: LogDisruptorMqMessageHandlerStrategyImpl.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Disruptor 日志消息队列处理策略实现类
 * @date: 2026-03-24
 */
@Slf4j
@Component
public class LogDisruptorMqMessageHandlerStrategyImpl implements DisruptorMqMessageHandlerStrategy {

    /**
     * 日志服务接口对象
     */
    private final IOperationRecordService operationRecordService;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入日志服务接口对象
     *
     * @param operationRecordService 日志服务接口对象, {@link IOperationRecordService}
     */
    @Autowired
    @SuppressWarnings("all")
    public LogDisruptorMqMessageHandlerStrategyImpl(IOperationRecordService operationRecordService) {
        this.operationRecordService = operationRecordService;
    }

    /**
     * 获取消息类型
     *
     * @return 消息类型枚举对象 {@link MqMessageTypeEnum}
     */
    @Override
    public MqMessageTypeEnum getMessageType() {
        return MqMessageTypeEnum.LOG;
    }

    /**
     * 执行消息处理逻辑
     *
     * @param messageDTO 消息事件对象 {@link BaseMessageDTO}
     */
    @Override
    public void execute(BaseMessageDTO messageDTO) {
        log.debug("Log Disruptor 消息队列策略开始处理, Start Execute, Current Event Object: {}", messageDTO);
        Boolean saveResult = operationRecordService.saveOneOperationRecord(
                MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_NAME_KEY),
                MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_TYPE_KEY),
                MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_PARAM_KEY),
                MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_OPERATOR_KEY),
                MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_MODULE_KEY));
        if (!saveResult) {
            log.error("同步保存操作日志记录失败, 使用文本记录模式记录日志!");
        }
        log.debug("Log Disruptor 消息队列策略处理完成, End Execute.");
    }
}
