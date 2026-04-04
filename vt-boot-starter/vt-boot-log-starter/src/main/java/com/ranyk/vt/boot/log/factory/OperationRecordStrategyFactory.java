package com.ranyk.vt.boot.log.factory;

import com.ranyk.vt.boot.log.annotations.OperationRecord;
import com.ranyk.vt.boot.log.strategy.OperationRecordStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CLASS_NAME: OperationRecordStrategyFactory.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作日志策略工厂类
 * @date: 2026-03-24
 */
@Slf4j
@Component
public class OperationRecordStrategyFactory {

    /**
     * 静态缓存策略对象
     */
    private final ConcurrentHashMap<OperationRecord.SaveMethod, OperationRecordStrategy> STRATEGY_CACHE_MAP = new ConcurrentHashMap<>(OperationRecord.SaveMethod.values().length + 1);

    /**
     * 构造函数 - 向 Spring  IOC 容器中注册操作日志策略对象
     *
     * @param operationRecordStrategyList 操作日志策略列表
     */
    @Autowired
    public OperationRecordStrategyFactory(List<OperationRecordStrategy> operationRecordStrategyList) {
        operationRecordStrategyList.forEach(strategy -> STRATEGY_CACHE_MAP.put(strategy.getSaveMethod(), strategy));
    }

    /**
     * 执行保存操作记录方法
     *
     * @param saveMethod      保存操作记录方法
     * @param paramMap        参数映射 Map 集合
     * @param operationRecord 操作记录注解对象
     */
    public void execute(OperationRecord.SaveMethod saveMethod, Map<String, Object> paramMap, OperationRecord operationRecord) {
        log.debug("Start Execute Operation Record Strategy. Current saveMethod: {}", saveMethod);
        OperationRecordStrategy operationRecordStrategy = STRATEGY_CACHE_MAP.get(saveMethod);
        if (Objects.isNull(operationRecordStrategy)) {
            log.error("Operation Record Strategy is null, Please Check the Configuration.");
        } else {
            operationRecordStrategy.execute(paramMap, operationRecord);
        }
        log.debug("End Execute Operation Record Strategy.");
    }
}
