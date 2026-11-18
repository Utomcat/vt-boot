package com.ranyk.vt.boot.satoken.config;

import cn.dev33.satoken.stp.StpInterface;
import com.ranyk.vt.boot.satoken.config.properties.SaTokenProperties;
import com.ranyk.vt.boot.satoken.handler.StpInterfaceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: SaTokenPermissionConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: sa-token 权限数据加载自动配置类, 注册默认的 {@link StpInterface} 实现,
 * 应用可通过自行注册 {@link StpInterface} Bean 覆盖默认实现
 * @date: 2026-11-17
 */
@AutoConfiguration
@AutoConfigureAfter(SaTokenConfigure.class)
@EnableConfigurationProperties(SaTokenProperties.class)
@ConditionalOnProperty(name = "sa-token.enable", havingValue = "true")
public class SaTokenPermissionConfiguration {

    /**
     * 向 Spring IOC 容器中注册 Sa-Token 权限数据加载 Bean
     *
     * @param saTokenProperties SA-TOKEN 配置属性对象
     * @return 权限数据加载对象 {@link StpInterface}
     */
    @Bean
    @ConditionalOnMissingBean(StpInterface.class)
    public StpInterface stpInterface(SaTokenProperties saTokenProperties) {
        return new StpInterfaceImpl(saTokenProperties);
    }
}
