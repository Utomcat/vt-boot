package com.ranyk.vt.boot.log.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.ranyk.vt.boot.base.context.UserContext;
import com.ranyk.vt.boot.log.annotations.OperationRecord;
import com.ranyk.vt.boot.log.strategy.OperationRecordStrategy;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * CLASS_NAME: SyncOperationRecordSave.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 同步操作日志记录保存策略实现类
 * @date: 2026-03-24
 */
@Slf4j
@Component
public class SyncOperationRecordSave implements OperationRecordStrategy {

    /**
     * 操作日志记录业务逻辑接口对象
     */
    private final IOperationRecordService operationRecordService;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入操作日志记录业务逻辑接口对象
     *
     * @param operationRecordService 操作日志记录业务逻辑接口对象, {@link IOperationRecordService}
     */
    @Autowired
    public SyncOperationRecordSave(IOperationRecordService operationRecordService) {
        this.operationRecordService = operationRecordService;
    }

    /**
     * 获取保存操作记录方法
     *
     * @return 保存操作记录方法 {@link OperationRecord.SaveMethod#SYNC}
     */
    @Override
    public OperationRecord.SaveMethod getSaveMethod() {
        return OperationRecord.SaveMethod.SYNC;
    }

    /**
     * 执行保存操作记录方法
     *
     * @param paramMap        参数映射 Map 集合
     * @param operationRecord 操作记录注解对象
     */
    @Override
    public void execute(Map<String, Object> paramMap, OperationRecord operationRecord) {
        log.debug("Start Execute Sync Operation Record Save Logic execute.");
        if (operationRecord.isSaveOperationRecord()) {
            Boolean saveResult = operationRecordService.saveOneOperationRecord(operationRecord.desc(), operationRecord.type().name(), JSONUtil.toJsonStr(paramMap), UserContext.getUserId(), operationRecord.module());
            if (!saveResult){
                log.error("同步保存操作日志记录失败, 使用文本记录模式记录日志!");
            }
        }
        log.debug("{} 在 {} 时, {} - {} 操作, 进行同步操作日志保存, 需要保存至 {} 表, 本次操作的参数是: {}", UserContext.getUserId(), LocalDateTime.now(), operationRecord.desc(), operationRecord.type(), operationRecord.module(), JSONUtil.toJsonStr(paramMap));
        log.debug("End Execute Sync Operation Record Save Logic execute.");
    }
}
