package com.ranyk.vt.boot.async.config;

import com.ranyk.vt.boot.async.pool.VtExecutors;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * CLASS_NAME: VtAsyncAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 异步配置类
 * @date: 2026-02-09
 */
@EnableAsync
@AutoConfiguration
@ConditionalOnBooleanProperty(prefix = "vt.async", name = "enabled", matchIfMissing = true)
public class VtAsyncAutoConfiguration {

    /**
     * 注册虚拟线程执行器
     * <p>
     * 作为 {@link Primary} 执行器供 {@code @Async}、异步任务等场景使用;
     * 使用方如需替换, 自行注册名为 {@code virtualThreadExecutor} 的 {@link Executor} Bean 即可覆盖。
     * </p>
     *
     * @return 虚拟线程执行器对象 {@link Executor}
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "virtualThreadExecutor")
    public Executor virtualThreadExecutor() {
        return VtExecutors.newVirtualThreadExecutor();
    }
}
