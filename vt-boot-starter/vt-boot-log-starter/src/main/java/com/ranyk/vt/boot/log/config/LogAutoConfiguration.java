package com.ranyk.vt.boot.log.config;

import com.ranyk.vt.boot.log.handle.LogAspectHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

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
    public LogAspectHandle logAspectHandle() {
        log.debug("Log stater injected successful. Register log aspect handle");
        return new LogAspectHandle();
    }
}
