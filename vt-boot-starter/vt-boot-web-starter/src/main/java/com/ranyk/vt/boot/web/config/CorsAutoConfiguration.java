package com.ranyk.vt.boot.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CLASS_NAME: CorsConfig.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 跨域访问自动配置类
 * @date: 2026-02-28
 */
@AutoConfiguration
public class CorsAutoConfiguration {

    /**
     * 向 Spring IOC 容器中注册 CORS 跨域过滤器对象
     *
     * @return CORS 跨域过滤器对象 {@link CorsFilter}
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许的前端域名正则匹配式
        config.addAllowedOriginPattern("*");
        // 允许携带认证信息（Token/Cookie）
        config.setAllowCredentials(true);
        // 允许的请求头
        config.addAllowedHeader("*");
        // 允许的请求方法
        config.addAllowedMethod("*");
        // 有效期 3600 s
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口生效
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
