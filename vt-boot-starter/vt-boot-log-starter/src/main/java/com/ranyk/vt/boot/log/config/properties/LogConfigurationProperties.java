package com.ranyk.vt.boot.log.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: LogConfigurationProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 日志配置属性类
 * @date: 2026-04-08
 */
@Data
@Component
@ConfigurationProperties(prefix = "vt.boot.log")
public class LogConfigurationProperties {

    /**
     * 是否启用操作记录 - 启用则需要配置 IOperationRecordService 接口的实现 Bean
     */
    private Boolean operationRecordEnabled = Boolean.TRUE;
}
