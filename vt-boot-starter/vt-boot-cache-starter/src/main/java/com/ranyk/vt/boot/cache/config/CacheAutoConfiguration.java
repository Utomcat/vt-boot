package com.ranyk.vt.boot.cache.config;

import com.ranyk.vt.boot.cache.config.properties.CacheConfigurationProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * CLASS_NAME: CacheAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 缓存配置类
 * @date: 2026-02-09
 */
@AutoConfiguration
@EnableConfigurationProperties(value = CacheConfigurationProperties.class)
@Import(value= {com.ranyk.vt.boot.cache.config.RedisAutoConfiguration.class, com.ranyk.vt.boot.cache.config.CaffeineAutoConfiguration.class})
public class CacheAutoConfiguration {


}
