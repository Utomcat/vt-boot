package com.ranyk.vt.boot.openapi.config;

import com.ranyk.vt.boot.openapi.config.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * CLASS_NAME: OpenApiAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: OpenAPI 文档自动配置类, 应用引入本 starter 后自动装配接口文档信息,
 * 应用可通过自行注册 {@link OpenAPI} Bean 覆盖默认实现
 * @date: 2026-11-17
 */
@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
@EnableConfigurationProperties(OpenApiProperties.class)
@ConditionalOnProperty(prefix = "vt.boot.openapi", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OpenApiAutoConfiguration {

    /**
     * 构建 OpenAPI 文档信息
     *
     * @param properties OpenAPI 文档配置属性
     * @return OpenAPI 对象 {@link OpenAPI}
     */
    @Bean
    @ConditionalOnMissingBean(OpenAPI.class)
    public OpenAPI openAPI(OpenApiProperties properties) {
        Contact contact = new Contact()
                .name(properties.getContactName())
                .email(properties.getContactEmail())
                .url(properties.getContactUrl());
        return new OpenAPI().info(new Info()
                .title(properties.getTitle())
                .version(properties.getVersion())
                .description(properties.getDescription())
                .contact(contact));
    }
}
