package com.ranyk.vt.boot.web.config;

import com.ranyk.vt.boot.web.handler.GlobalWebExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CLASS_NAME: WebMvcConfig.java
 *
 * @author ranyk
 * @version V1.0
 * @description: web 拦截配置类
 * @date: 2026-02-06
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 全局异常处理类对象 Bean 注册
     *
     * @return 全局异常处理类对象 {@link GlobalWebExceptionHandler}
     */
    @Bean
    @ConditionalOnMissingBean
    GlobalWebExceptionHandler globalWebExceptionHandler() {
        return new GlobalWebExceptionHandler();
    }


}
