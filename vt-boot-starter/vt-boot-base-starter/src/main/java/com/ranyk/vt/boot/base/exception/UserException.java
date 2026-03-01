package com.ranyk.vt.boot.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

/**
 * CLASS_NAME: UserException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自定义用户异常类
 * @date: 2026-02-28
 */
@Data
@ToString
@NoArgsConstructor
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
public class UserException extends BaseException {
    @Serial
    private static final long serialVersionUID = -7941765945566402377L;

    /**
     * 构造函数 - 传入 错误信息 构造
     *
     * @param detailMessage 错误信息
     */
    public UserException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 构造函数 - 传入 模块 、 错误信息 构造
     *
     * @param module        模块
     * @param detailMessage 错误信息
     */
    public UserException(String module, String detailMessage) {
        super(module, detailMessage);
    }

    /**
     * 构造函数 - 传入 错误代码 、 错误信息参数 构造
     *
     * @param code 错误代码
     * @param args 错误信息参数
     */
    public UserException(String code, Object[] args) {
        super(code, args);
    }

    /**
     * 构造函数 - 传入 模块 、 错误代码 、 错误信息参数 构造
     *
     * @param module 模块
     * @param code   错误代码
     * @param args   错误信息参数
     */
    public UserException(String module, String code, Object[] args) {
        super(module, code, args);
    }
}
