package com.ranyk.vt.boot.aop.handle;

import cn.hutool.core.util.ReflectUtil;
import com.ranyk.vt.boot.aop.annotations.AutoFillField;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * CLASS_NAME: AutoFillFieldAspect.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自动填充字段 AOP 切面处理器
 * @date: 2026-03-06
 */
@Slf4j
@Aspect
@Component
public class AutoFillFieldAspect {

    @Before("@annotation(autoFillField)")
    public void beforeAutoFillField(JoinPoint joinPoint, AutoFillField autoFillField) throws Throwable {
        log.debug("===== 【 AutoFillFieldAspect 】 开始执行自动填充字段");
        // 获取目标方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取目标方法
        Method method = signature.getMethod();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        if (Objects.isNull(args) || args.length == 0) {
            log.warn("方法 {} 没有参数，跳过自动填充", method.getName());
            return;
        }

        // 获取配置的参数类型
        Class<?> paramType = autoFillField.type();

        // 遍历方法参数，找到匹配的类型
        for (Object arg : args) {
            if (arg != null && (paramType.isInstance(arg) || arg.getClass().equals(paramType))) {
                // 设置需要填充的字段
                fillFields(arg, autoFillField);
                break;
            }
        }

        log.debug("===== 【AutoFillFieldAspect】自动填充字段执行完成");
    }

    /**
     * 填充字段值
     *
     * @param target        目标对象
     * @param autoFillField 自动填充注解
     */
    private void fillFields(Object target, AutoFillField autoFillField) {
        if (Objects.isNull(target)) {
            return;
        }
        // 获取需要填充的字段名
        String[] fields = autoFillField.fields();
        // 获取字段类型
        Class<?>[] classes = autoFillField.classes();
        // 验证字段名和类型数量是否匹配
        if (fields.length != classes.length) {
            log.warn("字段名数量 ({}) 与字段类型数量 ({}) 不匹配，使用较小值进行填充", fields.length, classes.length);
        }
        int minLength = Math.min(fields.length, classes.length);
        // 依次填充每个字段
        for (int i = 0; i < minLength; i++) {
            String fieldName = fields[i];
            Class<?> fieldType = classes[i];
            setFieldValue(target, fieldName, fieldType);
        }
        // 如果需要填充租户 ID，则填充租户 ID 字段
        if (autoFillField.isFillTenantId()) {
            String tenantIdName = autoFillField.tenantIdName();
            setTenantId(target, tenantIdName, autoFillField.tenantValue(), String.class);
        }
    }

    /**
     * 设置单个字段值
     *
     * @param target    目标对象
     * @param fieldName 字段名
     * @param fieldType 字段类型
     */
    private void setFieldValue(Object target, String fieldName, Class<?> fieldType) {
        try {
            // 检查字段是否存在
            Field field = ReflectUtil.getField(target.getClass(), fieldName);
            if (field == null) {
                log.debug("字段 {}.{} 不存在，跳过填充", target.getClass().getName(), fieldName);
                return;
            }
            // 检查字段是否已有值
            field.setAccessible(true);
            Object currentValue = field.get(target);
            if (Objects.nonNull(currentValue)) {
                log.debug("字段 {}.{} 已有值 [{}]，跳过填充", target.getClass().getName(), fieldName, currentValue);
                return;
            }
            // 根据字段类型生成默认值
            Object defaultValue = getDefaultValue(fieldType);
            if (Objects.nonNull(defaultValue)) {
                field.set(target, defaultValue);
                log.debug("自动填充字段：{}.{} = {} (类型：{})", target.getClass().getName(), fieldName, defaultValue, fieldType.getSimpleName());
            }
        } catch (IllegalAccessException e) {
            log.error("设置字段值失败：{}.{}", target.getClass().getName(), fieldName, e);
        } catch (Exception e) {
            log.error("处理字段异常：{}.{}", target.getClass().getName(), fieldName, e);
        }
    }

    /**
     * 设置租户字段值
     *
     * @param target       目标对象
     * @param fieldName    字段名
     * @param tenantValue  租户值
     * @param fieldType    字段类型
     */
    private void setTenantId(Object target, String fieldName, String tenantValue, Class<?> fieldType) {
        try {
            // 检查字段是否存在
            Field field = ReflectUtil.getField(target.getClass(), fieldName);
            if (field == null) {
                log.debug("租户字段 {}.{} 不存在，跳过填充", target.getClass().getName(), fieldName);
                return;
            }
            // 检查字段是否已有值
            field.setAccessible(true);
            Object currentValue = field.get(target);
            if (Objects.nonNull(currentValue)) {
                log.debug("租户字段 {}.{} 已有值 [{}]，跳过填充", target.getClass().getName(), fieldName, currentValue);
                return;
            }
            // 根据字段类型生成默认值
            field.set(target, tenantValue);
            log.debug("自动填充租户字段：{}.{} = {} (类型：{})", target.getClass().getName(), fieldName, tenantValue, fieldType.getSimpleName());
        } catch (IllegalAccessException e) {
            log.error("设置租户字段值失败：{}.{}", target.getClass().getName(), fieldName, e);
        } catch (Exception e) {
            log.error("处理租户字段填充异常：{}.{}", target.getClass().getName(), fieldName, e);
        }
    }

    /**
     * 根据字段类型获取默认值
     *
     * @param fieldType 字段类型
     * @return 默认值
     */
    private Object getDefaultValue(Class<?> fieldType) {
        if (fieldType == String.class) {
            return "-";
        } else if (fieldType == LocalDateTime.class) {
            return LocalDateTime.now();
        } else if (fieldType == java.util.Date.class) {
            return new java.util.Date();
        } else if (fieldType == java.sql.Timestamp.class) {
            return new java.sql.Timestamp(System.currentTimeMillis());
        } else if (fieldType == Integer.class || fieldType == int.class) {
            return 1;
        } else if (fieldType == Long.class || fieldType == long.class) {
            return 1L;
        }

        log.debug("不支持的字段类型：{}, 跳过填充", fieldType.getName());
        return null;
    }
}
