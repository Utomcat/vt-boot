package com.ranyk.vt.boot.satoken.config;

import cn.dev33.satoken.config.SaTokenConfig;
import com.ranyk.vt.boot.satoken.config.properties.SaTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * CLASS_NAME: SaTokenConfigure.java
 *
 * @author ranyk
 * @version V1.0
 * @description: sa-token 自动配置类
 * @date: 2026-02-28
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(SaTokenProperties.class)
@ConditionalOnProperty(name = "sa-token.enable", havingValue = "true")
public class SaTokenConfigure {

    /**
     * SA-TOKEN 配置属性对象
     */
    private final SaTokenProperties saTokenProperties;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入 SA-TOKEN 配置属性对象
     *
     * @param saTokenProperties SA-TOKEN 配置属性对象
     */
    @Autowired
    public SaTokenConfigure(SaTokenProperties saTokenProperties) {
        this.saTokenProperties = saTokenProperties;
    }

    /**
     * 向 Spring IOC 容器中注册 SA-TOKEN 配置 Bean 对象
     *
     * @return SA-TOKEN 配置对象 {@link SaTokenConfig}
     */
    @Bean
    @Primary
    public SaTokenConfig saTokenConfig() {
        log.info("SA-TOKEN is enabled. create SaTokenConfig Object");
        return new SaTokenConfig()
                // token 名称（同时也是 cookie 名称）
                .setTokenName(saTokenProperties.getTokenName())
                // token 有效期（单位：秒），默认30天，-1代表永不过期
                .setTimeout(saTokenProperties.getTimeout())
                // token 最低活跃频率（单位：秒），如果 token 超过此时间没有访问系统就会被冻结，默认-1 代表不限制，永不冻结
                .setActiveTimeout(saTokenProperties.getActivityTimeout())
                // 是否允许同一账号多地同时登录（为 true 时允许一起登录，为 false 时新登录挤掉旧登录）
                .setIsConcurrent(saTokenProperties.getIsConcurrent())
                // 在多人登录同一账号时，是否共用一个 token （为 true 时所有登录共用一个 token，为 false 时每次登录新建一个 token）
                .setIsShare(saTokenProperties.getIsShare())
                // token 风格
                .setTokenStyle(saTokenProperties.getTokenStyle())
                // 是否输出操作日志
                .setIsLog(saTokenProperties.getIsLog())
                // 是否在初始化配置时在控制台打印版本字符画
                .setIsPrint(saTokenProperties.getIsPrint());
    }

}
