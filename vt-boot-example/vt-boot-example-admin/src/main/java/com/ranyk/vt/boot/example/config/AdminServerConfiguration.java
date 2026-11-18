package com.ranyk.vt.boot.example.config;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Configuration;

/**
 * CLASS_NAME: AdminServerConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 示例工程监控台配置 - 开启 Spring Boot Admin 服务端
 * @date: 2026-11-18
 */
@Configuration(proxyBeanMethods = false)
@EnableAdminServer
public class AdminServerConfiguration {
}
