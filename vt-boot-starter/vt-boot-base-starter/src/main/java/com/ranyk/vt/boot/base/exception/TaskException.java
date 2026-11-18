package com.ranyk.vt.boot.base.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * CLASS_NAME: TaskException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务异常类
 * @date: 2026-11-18
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TaskException extends BaseException {

    @Serial
    private static final long serialVersionUID = 6074258160377128585L;

    /**
     * 构造函数 - 传入 异常信息 构造
     *
     * @param detailMessage 异常信息
     */
    public TaskException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 构造函数 - 传入 异常信息 与 原始异常 构造
     *
     * @param detailMessage 异常信息
     * @param e             原始异常
     */
    public TaskException(String detailMessage, Throwable e) {
        super(detailMessage, e);
    }

    /**
     * 构造函数 - 传入 异常代码 与 错误信息参数 构造
     *
     * @param code 错误代码
     * @param args 错误信息参数
     */
    public TaskException(String code, Object[] args) {
        super(code, args);
    }
}
