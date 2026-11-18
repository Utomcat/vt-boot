package com.ranyk.vt.boot.cache.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CLASS_NAME: CacheConfigurationProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 缓存配置属性对象
 * @date: 2025-09-26
 */
@Data
@ConfigurationProperties(prefix = "vt.cache")
public class CacheConfigurationProperties {

    /**
     * 是否启用缓存, 默认启用 (引入即用)
     */
    private Boolean enabled = Boolean.TRUE;
    /**
     * 是否启用 Caffeine 本地缓存, 默认启用 (本地缓存无外部依赖)
     */
    private Boolean isCaffeineEnabled = Boolean.TRUE;
    /**
     * Caffeine 本地缓存初始化大小
     */
    private Integer caffeineInitSize = 100;
    /**
     * Caffeine 本地缓存最大缓存条数
     */
    private Integer caffeineMaxSize = 500;
    /**
     * 是否输出缓存移除日志
     */
    private Boolean isOutPutLogWarn = Boolean.FALSE;
    /**
     * 是否启用 Redis 缓存
     */
    private Boolean isRedisEnabled = Boolean.FALSE;
}
