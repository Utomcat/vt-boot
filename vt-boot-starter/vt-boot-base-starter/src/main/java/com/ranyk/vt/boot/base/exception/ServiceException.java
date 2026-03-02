package com.ranyk.vt.boot.base.exception;

import lombok.*;

import java.io.Serial;

/**
 * CLASS_NAME: ServiceException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自定义业务异常类
 * @date: 2026-02-07
 */
@Data
@ToString
@NoArgsConstructor
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends BaseException {

    @Serial
    private static final long serialVersionUID = -4063634724930884784L;

    /**
     * 构造函数 - 传入 异常信息 构造
     *
     * @param detailMessage 异常信息
     */
    public ServiceException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 构造函数 - 传入 错误信息 、 错误信息 构造
     *
     * @param detailMessage 错误信息
     * @param e             错误信息
     */
    public ServiceException(String detailMessage, Throwable e) {
        super(detailMessage, e);
    }

    /**
     * 构造函数 - 传入 模块 、 异常信息 构造
     *
     * @param module        模块
     * @param detailMessage 错误信息
     */
    public ServiceException(String module, String detailMessage) {
        super(module, detailMessage);
    }

    /**
     * 构造函数 - 传入 异常代码 、 错误信息参数 构造
     *
     * @param code 错误代码
     * @param args 错误信息参数
     */
    public ServiceException(String code, Object[] args) {
        super(code, args);
    }

    /**
     * 构造函数 - 传入 模块 、 异常代码 、 异常信息参数 构造
     *
     * @param module 模块
     * @param code   异常代码
     * @param args   异常信息参数
     */
    public ServiceException(String module, String code, Object[] args) {
        super(module, code, args);
    }

}
