package com.ranyk.vt.boot.base.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;

/**
 * CLASS_NAME: CacheException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自定义缓存异常类
 * @date: 2026-03-02
 */
@Data
@NoArgsConstructor
@SuppressWarnings("unused")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CacheException extends BaseException {
    @Serial
    private static final long serialVersionUID = -7800059697578230752L;

    /**
     * 构造函数 - 传入 错误信息 构造
     *
     * @param detailMessage 错误信息
     */
    public CacheException(String detailMessage) {
        super("vt-boot-cache-starter", detailMessage);
    }

    /**
     * 构造函数 - 传入 错误信息 、 错误信息 构造
     *
     * @param detailMessage 错误信息
     * @param e             错误信息
     */
    public CacheException(String detailMessage, Throwable e) {
        super("vt-boot-cache-starter", detailMessage, e);
    }

    /**
     * 构造函数 - 传入 模块 、 错误信息 构造
     *
     * @param module        模块
     * @param detailMessage 错误信息
     */
    public CacheException(String module, String detailMessage) {
        super(module, detailMessage);
    }

    /**
     * 构造函数 - 传入 错误代码 、 错误信息参数 构造
     *
     * @param code 错误代码
     * @param args 错误信息参数
     */
    public CacheException(String code, Object[] args) {
        super(code, args);
    }

    /**
     * 构造函数 - 传入 模块 、 错误代码 、 错误信息参数 构造
     *
     * @param module 模块
     * @param code   错误代码
     * @param args   错误信息参数
     */
    public CacheException(String module, String code, Object[] args) {
        super(module, code, args);
    }
}
