package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: TaskErrorCodeEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务相关错误码枚举类, 枚举值同时作为国际化消息的 Key 使用
 * @date: 2026-11-18
 */
@Getter
public enum TaskErrorCodeEnum {
    /**
     * 任务不存在
     */
    TASK_NOT_EXISTS("task.not.exists"),
    /**
     * 任务名称不能为空
     */
    TASK_NAME_NOT_BLANK("task.name.not.blank"),
    /**
     * cron 表达式不合法
     */
    TASK_CRON_INVALID("task.cron.invalid"),
    /**
     * 任务执行器不存在
     */
    TASK_EXECUTOR_NOT_EXISTS("task.executor.not.exists"),
    /**
     * 任务操作失败
     */
    TASK_OPERATION_FAIL("task.operation.fail"),
    ;

    /**
     * 错误码值 - 同时作为国际化消息的 Key
     */
    private final String value;

    /**
     * 构造函数
     *
     * @param value 错误码值
     */
    TaskErrorCodeEnum(String value) {
        this.value = value;
    }
}
