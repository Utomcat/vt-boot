package com.ranyk.vt.boot.async.config;

import com.ranyk.vt.boot.async.pool.VtExecutors;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
public class VtAsyncAutoConfiguration {

    /**
     * 注册虚拟线程执行器
     *
     * @return 虚拟线程执行器对象 {@link Executor}
     */
    @Bean
    @Primary
    public Executor virtualThreadExecutor() {
        return VtExecutors.newVirtualThreadExecutor();
    }
}
