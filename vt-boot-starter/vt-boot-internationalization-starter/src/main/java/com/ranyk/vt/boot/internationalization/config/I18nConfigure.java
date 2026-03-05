package com.ranyk.vt.boot.internationalization.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * CLASS_NAME: I18nConfig.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 语言环境配置类
 * @date: 2025-12-18
 */
@Slf4j
@AutoConfiguration
public class I18nConfigure implements WebMvcConfigurer{

    /**
     * Add Spring MVC lifecycle interceptors for pre- and post-processing of
     * controller method invocations and resource handler requests.
     * Interceptors can be registered to apply to all requests or be limited
     * to a subset of URL patterns.
     *
     * @param registry 拦截器注册对象 {@link InterceptorRegistry}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("I18nConfigure configure. add LocaleChangeInterceptor");
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // 请求参数名，如?lang=zh_CN
        interceptor.setParamName("lang");
        registry.addInterceptor(interceptor);
    }
}
