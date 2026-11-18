package com.ranyk.vt.boot.datasource.config.properties;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

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
    /**
     * 不需要进行自动插入租户ID的表名
     */
    private List<String> ignoreTable = new ArrayList<>();
    /**
     * 数据库类型 - 用于 MyBatis-Plus 分页插件识别方言, 默认 MYSQL
     */
    private DbType dbType = DbType.MYSQL;
}
