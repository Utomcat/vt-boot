package com.ranyk.vt.boot.task.constant;

import lombok.Getter;

/**
 * CLASS_NAME: MisfirePolicyEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务错过触发(misfire)处理策略枚举
 * @date: 2026-11-18
 */
@Getter
public enum MisfirePolicyEnum {
    /**
     * 忽略错过的时间, 立即执行一次后按原计划继续
     */
    FIRE_AND_PROCEED("忽略错过的触发, 立即补执行一次后继续"),
    /**
     * 不补执行, 直接按原计划等待下一次触发
     */
    DO_NOTHING("不补执行, 等待下一次触发"),
    /**
     * 丢弃错过的触发, 不做任何处理
     */
    IGNORE("丢弃错过的触发"),
    ;

    /**
     * 策略说明
     */
    private final String desc;

    /**
     * 构造函数
     *
     * @param desc 策略说明
     */
    MisfirePolicyEnum(String desc) {
        this.desc = desc;
    }
}
