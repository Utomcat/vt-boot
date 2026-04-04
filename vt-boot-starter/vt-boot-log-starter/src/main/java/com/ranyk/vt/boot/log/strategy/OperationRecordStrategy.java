package com.ranyk.vt.boot.log.strategy;

import com.ranyk.vt.boot.log.annotations.OperationRecord;

import java.util.Map;

/**
 * CLASS_NAME: OperationRecordStrategy.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作日志策略接口类
 * @date: 2026-03-24
 */
public interface OperationRecordStrategy {

    /**
     * 获取保存操作记录方法
     *
     * @return 保存操作记录方法
     */
    OperationRecord.SaveMethod getSaveMethod();

    /**
     * 执行保存操作记录方法
     *
     * @param paramMap           参数映射 Map 集合
     * @param operationRecord    操作记录注解对象
     */
    void execute(Map<String, Object> paramMap, OperationRecord operationRecord);
}
