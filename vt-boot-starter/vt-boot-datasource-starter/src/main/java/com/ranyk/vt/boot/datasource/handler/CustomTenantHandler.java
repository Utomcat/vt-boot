package com.ranyk.vt.boot.datasource.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.ranyk.vt.boot.base.constant.AutoFillFieldEnum;
import com.ranyk.vt.boot.base.constant.TenantEnum;
import com.ranyk.vt.boot.base.context.TenantContext;
import com.ranyk.vt.boot.datasource.config.properties.DatasourceConfigurationProperties;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.schema.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * CLASS_NAME: CustomTenantHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自定义租户监听器
 * @date: 2026-03-03
 */
@Component
public class CustomTenantHandler implements TenantLineHandler {

    /**
     * 数据源配置属性对象
     */
    private final DatasourceConfigurationProperties datasourceConfigurationProperties;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入数据源配置属性对象
     *
     * @param datasourceConfigurationProperties 数据源配置属性对象
     */
    @Autowired
    public CustomTenantHandler(DatasourceConfigurationProperties datasourceConfigurationProperties) {
        this.datasourceConfigurationProperties = datasourceConfigurationProperties;
    }

    /**
     * 获取租户 ID 值表达式，只支持单个 ID 值
     * <p>
     *
     * @return 租户 ID 值表达式
     */
    @Override
    public Expression getTenantId() {
        return new LongValue(Optional.ofNullable(TenantContext.getTenantId()).filter(StrUtil::isNotBlank).orElse(String.valueOf(AutoFillFieldEnum.TENANT_ID.getDefaultValue())));
    }

    /**
     * 获取租户字段名
     * <p>
     * 默认字段名叫: tenant_id
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return TenantEnum.TENANT_ID_COLUMN.getValue();
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     * <p>
     * 默认都要进行解析并拼接多租户条件
     *
     * @param tableName 表名
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return datasourceConfigurationProperties.getIgnoreTable().contains(tableName);
    }

    /**
     * 忽略插入租户字段逻辑
     *
     * @param columns        插入字段
     * @param tenantIdColumn 租户 ID 字段
     * @return 是否忽略, true:表示忽略，false:需要插入租户字段
     */
    @Override
    public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
        return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
    }
}
