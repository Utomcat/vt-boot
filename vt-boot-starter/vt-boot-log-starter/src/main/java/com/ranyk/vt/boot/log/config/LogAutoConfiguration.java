package com.ranyk.vt.boot.log.config;

import com.ranyk.vt.boot.log.factory.OperationRecordStrategyFactory;
import com.ranyk.vt.boot.log.handle.LogAspectHandle;
import com.ranyk.vt.boot.log.handle.OperationRecordAspectHandle;
import com.ranyk.vt.boot.log.strategy.impl.AsyncOperationRecordSave;
import com.ranyk.vt.boot.log.strategy.impl.SyncOperationRecordSave;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.util.Arrays;

/**
 * CLASS_NAME: LogAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 日志自动配置类
 * @date: 2026-03-06
 */
@Slf4j
@AutoConfiguration
public class LogAutoConfiguration {

    /**
     * 向 Spring IOC 容器中注册日志切面处理类对象
     *
     * @return 日志切面处理类对象 {@link LogAspectHandle}
     */
    @Bean
    @ConditionalOnMissingBean
    public LogAspectHandle logAspectHandle() {
        log.debug("Log Stater Injected Successful. Register Log Annotation Aspect Handle.");
        return new LogAspectHandle();
    }

    /**
     * 向 Spring IOC 容器中注册操作记录切面处理类对象
     *
     * @return 操作记录切面处理类对象 {@link OperationRecordAspectHandle}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBooleanProperty(prefix = "vt.boot.log", name = "operation-record-enabled", matchIfMissing = true)
    public OperationRecordAspectHandle operationRecordAspectHandle(OperationRecordStrategyFactory operationRecordStrategyFactory) {
        log.debug("Log Stater Injected Successful. Register OperationRecord Annotation Aspect Handle.");
        return new OperationRecordAspectHandle(operationRecordStrategyFactory);
    }

    /**
     * 注册操作记录策略工厂（依赖策略实现类）
     *
     * @return 操作记录策略工厂 {@link OperationRecordStrategyFactory}
     */
    @Bean
    @ConditionalOnMissingBean
    @DependsOn(value = {"syncOperationRecordSave", "asyncOperationRecordSave"})
    @ConditionalOnBooleanProperty(prefix = "vt.boot.log", name = "operation-record-enabled", matchIfMissing = true)
    public OperationRecordStrategyFactory operationRecordStrategyFactory(SyncOperationRecordSave syncOperationRecordSave, AsyncOperationRecordSave asyncOperationRecordSave) {
        log.debug("Log Stater Injected Successful. Register Operation Record Strategy Factory.");
        // 手动注入策略实现类（也可通过 List<OperationRecordStrategy> 自动注入，需确保策略类被扫描）
        return new OperationRecordStrategyFactory(Arrays.asList(syncOperationRecordSave, asyncOperationRecordSave));
    }

    /**
     * 注册同步保存策略
     *
     * @return 同步保存策略 {@link SyncOperationRecordSave}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBooleanProperty(prefix = "vt.boot.log", name = "operation-record-enabled", matchIfMissing = true)
    public SyncOperationRecordSave syncOperationRecordSave(IOperationRecordService operationRecordService) {
        log.debug("Log Stater Injected Successful. Register Sync Operation Record Save Strategy.");
        return new SyncOperationRecordSave(operationRecordService);
    }

    /**
     * 注册异步保存策略
     *
     * @return 异步保存策略 {@link AsyncOperationRecordSave}
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBooleanProperty(prefix = "vt.boot.log", name = "operation-record-enabled", matchIfMissing = true)
    public AsyncOperationRecordSave asyncOperationRecordSave() {
        log.debug("Log Stater Injected Successful. Register Async Operation Record Save Strategy.");
        return new AsyncOperationRecordSave();
    }
}
