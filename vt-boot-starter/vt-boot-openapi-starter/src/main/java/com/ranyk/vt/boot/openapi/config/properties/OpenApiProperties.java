package com.ranyk.vt.boot.openapi.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CLASS_NAME: OpenApiProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: OpenAPI 文档配置属性类
 * @date: 2026-11-17
 */
@Data
@ConfigurationProperties(prefix = "vt.boot.openapi")
public class OpenApiProperties {

    /**
     * 是否启用 OpenAPI 文档自动配置, 默认启用
     */
    private Boolean enabled = Boolean.TRUE;
    /**
     * 文档标题
     */
    private String title = "vt-boot API 文档";
    /**
     * 文档版本
     */
    private String version = "1.0.0";
    /**
     * 文档描述
     */
    private String description = "vt-boot 接口文档";
    /**
     * 联系人名称
     */
    private String contactName = "ranyk";
    /**
     * 联系人邮箱
     */
    private String contactEmail;
    /**
     * 联系人主页
     */
    private String contactUrl;
}
