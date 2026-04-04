package com.ranyk.vt.boot.log.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.ranyk.vt.boot.base.constant.*;
import com.ranyk.vt.boot.base.context.UserContext;
import com.ranyk.vt.boot.log.annotations.OperationRecord;
import com.ranyk.vt.boot.log.strategy.OperationRecordStrategy;
import com.ranyk.vt.boot.mq.util.MqUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * CLASS_NAME: AsyncOperationRecordSave.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 异步操作日志保存策略实现类
 * @date: 2026-03-24
 */
@Slf4j
@Component
public class AsyncOperationRecordSave implements OperationRecordStrategy {

    /**
     * 获取保存操作记录方法
     *
     * @return 保存操作记录方法 {@link OperationRecord.SaveMethod#ASYNC}
     */
    @Override
    public OperationRecord.SaveMethod getSaveMethod() {
        return OperationRecord.SaveMethod.ASYNC;
    }

    /**
     * 执行保存操作记录方法
     *
     * @param paramMap        参数映射 Map 集合
     * @param operationRecord 操作记录注解对象
     */
    @Override
    public void execute(Map<String, Object> paramMap, OperationRecord operationRecord) {
        log.debug("Start Async Operation Record Save Logic execute.");
        // 在执行异步操作前，先获取并保存用户上下文信息
        String userId;
        try {
            userId = UserContext.getUserId();
        } catch (Exception e) {
            log.warn("获取用户上下文失败，可能是在异步线程中执行: {} ,准备使用默认值, 当前的异常栈是: ", e.getMessage(), e);
            userId = DefaultValueEnum.OPERATOR_DEFAULT.getValue();
        }
        if (operationRecord.isSaveOperationRecord()) {
            log.debug("进行异步日志操作保存.....");
            Map<String, Object> header = Map.of(MessageKeyEnum.MESSAGE_HEADER_TYPE_KEY.getValue(), MqMessageTypeEnum.LOG,
                    MessageKeyEnum.MESSAGE_HEADER_RABBITMQ_EXCHANGE_KEY.getValue(), RabbitmqKeyEnum.DEFAULT_RABBITMQ_EXCHANGE.getValue(),
                    MessageKeyEnum.MESSAGE_HEADER_RABBITMQ_ROUTING_KEY.getValue(), RabbitmqKeyEnum.DEFAULT_RABBITMQ_ROUTING_KEY.getValue());
            Map<String, Object> body = Map.of(MessageKeyEnum.MESSAGE_BODY_DATA_KEY.getValue(), Map.of(
                    MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_NAME_KEY.getValue(), operationRecord.desc(),
                    MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_TYPE_KEY.getValue(), operationRecord.type().name(),
                    MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_PARAM_KEY.getValue(), JSONUtil.toJsonStr(paramMap),
                    MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_OPERATOR_KEY.getValue(), userId,
                    MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_MODULE_KEY.getValue(), operationRecord.module()
            ));
            switch (operationRecord.asyncMethod()) {
                // 发布消息到 Disruptor 队列
                case DISRUPTOR -> MqUtils.publish(header, body);
                // 发布消息到 RabbitMQ 队列
                case RABBITMQ -> MqUtils.publish(header, body, MqTypeEnum.RABBITMQ);
                default ->
                        log.warn("Async Operation Record Save Logic execute, 异步保存操作记录方法 {} 正在开发中..., 敬请期待!", operationRecord.asyncMethod());
            }
        }
        log.debug("{} 在 {} 时, {} - {} 操作, 进行异步操作日志保存, 当前使用的异步方式是 {}, 本次操作的参数是 {}", userId, LocalDateTime.now(), operationRecord.desc(), operationRecord.type(), operationRecord.asyncMethod(), JSONUtil.toJsonStr(paramMap));
        log.debug("End Async Operation Record Save Logic execute.");
    }
}
