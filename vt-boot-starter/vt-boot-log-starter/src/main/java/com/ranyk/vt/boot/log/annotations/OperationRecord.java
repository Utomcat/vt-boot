package com.ranyk.vt.boot.log.annotations;

import com.ranyk.vt.boot.base.constant.MqTypeEnum;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;

import java.lang.annotation.*;

/**
 * CLASS_NAME: OperationRecord.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作记录注解, 使用该注解意味着需要记录操作日志
 * @date: 2026-03-24
 */
// 生成文档时包含该注解
@Documented
// 仅作用于方法
@Target({ElementType.METHOD})
// 运行时保留，允许AOP读取
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationRecord {

    /**
     * 操作描述 （必填）
     */
    String desc() default "";
    /**
     * 操作模块 （必填，默认 operation_log_info）该属性用于指定操作日志记录的数据表写入表名,
     * 主要用于用户后续对日志进行检索的筛选, 同时用于一些无用日志的操作记录屏蔽, 如果使用其他值, 请先确认对应的表是否存在,
     * 如果不存在请先创建对应的表, 表字段和表 operation_log_info 一致即可
     */
    String module() default "operation_log_info";
    /**
     * 操作类型 （可选，默认默认新增/保存）
     */
    OperateTypeEnum type() default OperateTypeEnum.SAVE;
    /**
     * 是否记录入参 （默认记录）
     */
    boolean recordParams() default true;
    /**
     * 是否保存操作记录到数据库 （默认不保存到数据库）
     */
    boolean isSaveOperationRecord() default false;
    /**
     * 保存操作记录方法 （sync: 同步保存(直接写入数据库, 默认); async: 异步保存(将数据写入消息队列, 异步处理)）
     */
    SaveMethod saveOperationRecordMethod() default SaveMethod.SYNC;
    /**
     * 异步保存操作记录方法（选项参见 {@link MqTypeEnum}，默认 {@link MqTypeEnum#DISRUPTOR}）
     */
    MqTypeEnum asyncMethod() default MqTypeEnum.DISRUPTOR;

    /**
     * 保存操作记录方法枚举类
     */
    enum SaveMethod {
        /**
         * 同步保存
         */
        SYNC,
        /**
         * 异步保存
         */
        ASYNC
    }
}
