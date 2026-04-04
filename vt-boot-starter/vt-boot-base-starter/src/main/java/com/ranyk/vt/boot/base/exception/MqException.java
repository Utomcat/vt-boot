package com.ranyk.vt.boot.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

/**
 * CLASS_NAME: MqException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自定义 MQ 消息队列异常
 * @date: 2026-03-24
 */
@Data
@NoArgsConstructor
@SuppressWarnings("unused")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MqException extends BaseException {
    @Serial
    private static final long serialVersionUID = 5741396312699767933L;

    /**
     * 构造函数 - 传入 错误信息 构造
     *
     * @param detailMessage 错误信息
     */
    public MqException(String detailMessage) {
        super("vt-boot-mq-starter", detailMessage);
    }

    /**
     * 构造函数 - 传入 错误信息 、 错误信息 构造
     *
     * @param detailMessage 错误信息
     * @param e             错误信息
     */
    public MqException(String detailMessage, Throwable e) {
        super("vt-boot-mq-starter", detailMessage, e);
    }

    /**
     * 构造函数 - 传入 模块 、 错误信息 构造
     *
     * @param module        模块
     * @param detailMessage 错误信息
     */
    public MqException(String module, String detailMessage) {
        super(module, detailMessage);
    }

    /**
     * 构造函数 - 传入 错误代码 、 错误信息参数 构造
     *
     * @param code 错误代码
     * @param args 错误信息参数
     */
    public MqException(String code, Object[] args) {
        super(code, args);
    }

    /**
     * 构造函数 - 传入 模块 、 错误代码 、 错误信息参数 构造
     *
     * @param module 模块
     * @param code   错误代码
     * @param args   错误信息参数
     */
    public MqException(String module, String code, Object[] args) {
        super(module, code, args);
    }
}
