package com.ranyk.vt.boot.log.handle;

import com.ranyk.vt.boot.log.annotations.OperationRecord;
import com.ranyk.vt.boot.log.factory.OperationRecordStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * CLASS_NAME: OperationRecordAspectHandle.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作记录注解 AOP 切面处理类
 * @date: 2026-03-24
 */
@Slf4j
// 标识为切面类
@Aspect
// 交给Spring容器管理
@Component
public class OperationRecordAspectHandle {

    /**
     * 操作记录策略工厂类
     */
    private final OperationRecordStrategyFactory operationRecordStrategyFactory;

    /**
     * 构造函数
     *
     * @param operationRecordStrategyFactory 操作记录策略工厂类
     */
    @Autowired
    public OperationRecordAspectHandle(OperationRecordStrategyFactory operationRecordStrategyFactory) {
        this.operationRecordStrategyFactory = operationRecordStrategyFactory;
    }

    /**
     * 配置切点
     */
    @Pointcut("@annotation(com.ranyk.vt.boot.log.annotations.OperationRecord)")
    public void operationRecordPointcut() {
    }

    /**
     * 后置返回通知：获取入参 + 返回值
     *
     * @param joinPoint       核心对象，包含方法所有信息
     * @param operationRecord 自定义注解
     * @param result          方法返回值（通过returning绑定）
     */
    @AfterReturning(pointcut = "operationRecordPointcut() && @annotation(operationRecord)", returning = "result")
    public void afterReturning(JoinPoint joinPoint, OperationRecord operationRecord, Object result) {
        log.debug("【{} 操作 {} 记录】开始记录操作日志...", operationRecord.desc(), operationRecord.type().name());
        // ========== 获取参数名+参数值的映射（更易读） ==========
        Map<String, Object> paramMap = new HashMap<>(2);
        if (operationRecord.recordParams()) {
            paramMap = getParamNameAndValue(joinPoint);
            log.debug("【详细入参】方法入参（键值对形式）：{}", paramMap);
        }
        operationRecordStrategyFactory.execute(operationRecord.saveOperationRecordMethod(), paramMap, operationRecord);
        // 其他增强逻辑（比如记录返回值）
        if (Objects.isNull(result)){
            log.debug("【方法返回值】：返回值是 void 或 为 null.");
        }
        else {
            log.debug("【方法返回值】：{} .", result);
        }
        log.debug("【{} 操作 {} 记录】结束记录操作日志...", operationRecord.desc(), operationRecord.type().name());
    }

    /**
     * 进阶：获取方法参数名 + 参数值的映射（解决仅数组不直观的问题）
     */
    private Map<String, Object> getParamNameAndValue(JoinPoint joinPoint) {
        Map<String, Object> paramMap = new HashMap<>();
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取参数名数组（需确保编译时保留参数名，Spring Boot默认支持）
        String[] paramNames = signature.getParameterNames();
        // 获取参数值数组
        Object[] paramValues = joinPoint.getArgs();
        // 组装参数名和参数值
        for (int i = 0; i < paramNames.length; i++) {
            paramMap.put(paramNames[i], paramValues[i]);
        }
        return paramMap;
    }

}
