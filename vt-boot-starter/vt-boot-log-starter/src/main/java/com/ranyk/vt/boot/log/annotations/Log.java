package com.ranyk.vt.boot.log.annotations;

import java.lang.annotation.*;

/**
 * CLASS_NAME: Log.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 日志注解
 * @date: 2026-02-09
 */
// 生成文档时包含该注解
@Documented
// 仅作用于方法
@Target({ElementType.METHOD})
// 运行时保留，允许AOP读取
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 操作描述（必填）
     */
    String operation() default "";
    /**
     * 日志类型（可选）
     */
    LogType type() default LogType.OPERATION;
    /**
     * 是否记录方法入参（默认记录）
     */
    boolean recordParams() default true;
    /**
     * 是否记录方法出参（默认记录）
     */
    boolean recordResult() default true;

    /**
     * 日志类型枚举
     */
    enum LogType {
        // 操作日志
        OPERATION,
        // 错误日志
        ERROR,
        // 访问日志
        ACCESS
    }
}
