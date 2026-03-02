package com.ranyk.vt.boot.cache.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;

/**
 * CLASS_NAME: RedisAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Redis 自动配置类
 * @date: 2026-03-02
 */
@AutoConfiguration
@ConditionalOnBooleanProperties(value = {
        @ConditionalOnBooleanProperty(prefix = "vt.cache", name = "enabled", matchIfMissing = true),
        @ConditionalOnBooleanProperty(prefix = "vt.cache", name = "is-redis-enabled", matchIfMissing = true)
})
public class RedisAutoConfiguration {

}
