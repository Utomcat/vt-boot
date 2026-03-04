package com.ranyk.vt.boot.datasource.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CLASS_NAME: DatasourceConfigurationProperties.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据源配置属性类
 * @date: 2026-03-03
 */
@Data
@ConfigurationProperties(prefix = "vt.boot")
public class DatasourceConfigurationProperties {

    /**
     * 是否启用租户
     */
    private Boolean isEnableTenant = Boolean.FALSE;
}
