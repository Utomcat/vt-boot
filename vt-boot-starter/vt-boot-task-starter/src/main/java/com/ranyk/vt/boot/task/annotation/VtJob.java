package com.ranyk.vt.boot.task.annotation;

import com.ranyk.vt.boot.task.constant.MisfirePolicyEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CLASS_NAME: VtJob.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务声明注解 - 标注在实现 VtJobExecutor 的 Bean 上, 由框架自动注册到 Quartz
 * @date: 2026-11-18
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VtJob {

    /**
     * 任务名称, 默认取 Bean 名称
     *
     * @return 任务名称
     */
    String name() default "";

    /**
     * 任务分组, 默认 DEFAULT
     *
     * @return 任务分组
     */
    String group() default "DEFAULT";

    /**
     * cron 表达式
     *
     * @return cron 表达式
     */
    String cron();

    /**
     * 任务描述
     *
     * @return 任务描述
     */
    String description() default "";

    /**
     * 错过触发的处理策略
     *
     * @return 错过触发的处理策略 {@link MisfirePolicyEnum}
     */
    MisfirePolicyEnum misfire() default MisfirePolicyEnum.DO_NOTHING;
}
