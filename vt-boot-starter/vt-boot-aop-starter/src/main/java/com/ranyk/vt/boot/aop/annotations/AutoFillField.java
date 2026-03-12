package com.ranyk.vt.boot.aop.annotations;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;

import java.lang.annotation.*;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: AutoFillFiled.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自动填充数据注解
 * @date: 2026-03-06
 */
// 生成文档时包含该注解
@Documented
// 仅作用于方法
@Target({ElementType.METHOD})
// 运行时保留，允许AOP读取
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFillField {

    /**
     * 当前方法的参数类型
     *
     * @return 参数类型 Class 对象, 默认 BaseDTO.class
     */
    Class<?> type() default BaseDTO.class;

    /**
     * 自动填充字段名
     *
     * @return 字段名称数组, 默认为 createBy, createTime, updateBy, updateTime
     */
    String[] fields() default {"createBy", "createTime", "updateBy", "updateTime"};

    /**
     * 自动填充字段类型
     *
     * @return 字段类型数组, 默认为 String.class, LocalDateTime.class, String.class, LocalDateTime.class
     */
    Class<?>[] classes() default {String.class, LocalDateTime.class, String.class, LocalDateTime.class};

    /**
     * 自动填充字段值
     *
     * @return 字段值数组, 默认值 0000000001
     */
    String[] value() default {};

    /**
     * 租户ID字段名
     *
     * @return 租户ID字段名, 默认为 tenantId
     */
    String tenantIdName() default "tenantId";

    /**
     * 是否填充租户ID字段
     *
     * @return 是否填充租户ID字段, 默认填充  true
     */
    boolean isFillTenantId() default true;

    /**
     * 租户ID字段值
     *
     * @return 租户ID字段值, 默认值 0000000001
     */
    String tenantValue() default "0000000001";
}
