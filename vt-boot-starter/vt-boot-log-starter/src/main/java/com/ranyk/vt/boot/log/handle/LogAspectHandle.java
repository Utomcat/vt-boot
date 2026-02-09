package com.ranyk.vt.boot.log.handle;

import com.ranyk.vt.boot.log.annotations.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * CLASS_NAME: LogAspectHandle.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Log 注解切面处理类
 * @date: 2026-02-09
 */
@Slf4j
// 标识为切面类
@Aspect
// 交给Spring容器管理
@Component
public class LogAspectHandle {

    /**
     * 定义切入点：拦截所有标注了@Log注解的方法
     */
    @Pointcut("@annotation(com.ranyk.vt.boot.log.annotations.Log)")
    public void logPointcut() {}

    /**
     * 环绕通知：在方法执行前后捕获日志信息
     *
     * @param joinPoint ProceedingJoinPoint 可以控制目标方法的执行（执行、跳过、重试等）
     * @return 返回目标方法的执行结果
     * @throws Throwable 抛出异常
     */
    @Around("logPointcut()")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取目标方法的信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getAnnotation(Log.class);
        // 2. 解析注解属性
        String operation = logAnnotation.operation();
        Log.LogType logType = logAnnotation.type();
        boolean recordParams = logAnnotation.recordParams();
        boolean recordResult = logAnnotation.recordResult();
        // 3. 记录方法开始执行的日志
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        log.info("===== 【{}】开始执行 - 类：{}，方法：{} 操作类型: {} =====", operation, className, methodName, logType);
        // 4. 记录入参（如果开启）
        if (recordParams) {
            Object[] args = joinPoint.getArgs();
            log.info("【{}】入参：{}", operation, Arrays.toString(args));
        }
        Object result;
        long startTime = System.currentTimeMillis();
        try {
            // 5. 执行目标方法
            result = joinPoint.proceed();
            // 6. 记录执行耗时和出参（如果开启）
            long costTime = System.currentTimeMillis() - startTime;
            log.info("===== 【{}】执行成功 - 耗时：{}ms =====", operation, costTime);
            if (recordResult) {
                log.info("【{}】出参：{}", operation, result);
            }
        } catch (Throwable throwable) {
            // 7. 捕获异常并记录
            long costTime = System.currentTimeMillis() - startTime;
            log.error("===== 【{}】执行失败 - 耗时：{}ms，异常：{} =====", operation, costTime, throwable.getMessage(), throwable);
            // 抛出异常，不影响原有业务逻辑
            throw throwable;
        }
        return result;
    }
}
