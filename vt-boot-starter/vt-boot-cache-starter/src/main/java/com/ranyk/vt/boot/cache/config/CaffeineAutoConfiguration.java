package com.ranyk.vt.boot.cache.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ranyk.vt.boot.cache.config.properties.CacheConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: CaffeineAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 本地缓存 Caffeine 自动配置
 * @date: 2026-03-02
 */
@Slf4j
@AutoConfiguration
@ConditionalOnBooleanProperties(value = {
        @ConditionalOnBooleanProperty(prefix = "vt.cache", name = "enabled", matchIfMissing = true),
        @ConditionalOnBooleanProperty(prefix = "vt.cache", name = "is-caffeine-enabled", matchIfMissing = true)
})
public class CaffeineAutoConfiguration {

    /**
     * 缓存配置属性对象
     */
    private final CacheConfigurationProperties cacheConfigurationProperties;

    /**
     * 缓存配置类对象构造方法 - 向 Spring IOC 容器中自动添加缓存配置属性对象
     *
     * @param cacheConfigurationProperties 缓存配置属性对象
     */
    @Autowired
    public CaffeineAutoConfiguration(CacheConfigurationProperties cacheConfigurationProperties) {
        this.cacheConfigurationProperties = cacheConfigurationProperties;
    }

    /**
     * 常规缓存,未设置指定的过期时间,如果需要设置过期时间,则需要另外配置一个缓存
     *
     * @return 返回构建好的缓存对象 {@link Cache}
     */
    @Bean("caffeineCache")
    public Cache<@NonNull String, Object> caffeineCache() {
        log.info("CaffeineCache is enabled. create Caffeine Cache Object");
        return Caffeine.newBuilder()
                // 初始的缓存空间大小
                .initialCapacity(cacheConfigurationProperties.getCaffeineInitSize())
                // 缓存的最大条数
                .maximumSize(cacheConfigurationProperties.getCaffeineMaxSize())
                // 缓存移除监听器
                .removalListener((key, value, cause) -> {
                    if (cacheConfigurationProperties.getIsOutPutLogWarn()) {
                        log.warn("caffeineCache Cache failure listening => key: {}, value: {}, cause: {}", key, value, cause);
                    }
                })
                // 开启统计功能
                .recordStats()
                .build();
    }
}
