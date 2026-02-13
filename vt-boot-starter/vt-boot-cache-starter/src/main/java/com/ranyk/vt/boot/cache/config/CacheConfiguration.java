package com.ranyk.vt.boot.cache.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;

/**
 * CLASS_NAME: CacheConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 缓存配置类
 * @date: 2026-02-09
 */
@AutoConfiguration
@ConditionalOnBooleanProperty(name="vt.cache.enabled", matchIfMissing = true)
public class CacheConfiguration {


}
