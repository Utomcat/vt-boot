package com.ranyk.vt.boot.task.constant;

import lombok.Getter;

/**
 * CLASS_NAME: JobLogStatusEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志状态枚举
 * @date: 2026-11-18
 */
@Getter
public enum JobLogStatusEnum {
    /**
     * 执行中
     */
    RUNNING(0, "执行中"),
    /**
     * 执行成功
     */
    SUCCESS(1, "执行成功"),
    /**
     * 执行失败
     */
    FAILED(2, "执行失败"),
    /**
     * 被否决(未执行)
     */
    VETOED(3, "被否决"),
    ;

    /**
     * 状态说明
     */
    private final String desc;

    /**
     * 状态值
     */
    private final Integer value;

    /**
     * 构造函数
     *
     * @param value 状态值
     * @param desc  状态说明
     */
    JobLogStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
