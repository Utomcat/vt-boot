package com.ranyk.vt.boot.datasource.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ranyk.vt.boot.datasource.constant.DataSourcesDefaultValueEnum;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * CLASS_NAME: DataObjectHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库对象监听实现类, 该类用于在插入和修改时对公共字段进行填充, 如创建时间、更新时间、创建人、更新人、数据状态、备注等字段进行填充,以确保数据的完整性和一致性。
 * @date: 2026-02-13
 */
public class DataObjectHandler implements MetaObjectHandler {

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时填充字段 - 数据创建时间
        this.setFieldValByNameIfNull("createTime", LocalDateTime.now(), metaObject);
        // 插入时填充字段 - 数据更新时间
        this.setFieldValByNameIfNull("updateTime", LocalDateTime.now(), metaObject);
        // 插入时填充字段 - 创建人 ID
        this.setFieldValByNameIfNull("createBy", DataSourcesDefaultValueEnum.CREATE_BY_DEFAULT_VALUE.getValue(), metaObject);
        // 插入时填充字段 - 更新人 ID
        this.setFieldValByNameIfNull("updateBy", DataSourcesDefaultValueEnum.UPDATE_BY_DEFAULT_VALUE.getValue(), metaObject);
        // 插入时填充字段 - 备注
        this.setFieldValByNameIfNull("remark", DataSourcesDefaultValueEnum.STRING_VALUE_DEFAULT_VALUE.getValue(), metaObject);
        // 插入时填充字段 - 数据状态
        this.setFieldValByNameIfNull("status", DataSourcesDefaultValueEnum.STATUS_DEFAULT_VALUE.getValue(), metaObject);
        // 插入时填充字段 - 租户 ID
        this.setFieldValByNameIfNull("tenantId", DataSourcesDefaultValueEnum.TENANT_ID_DEFAULT_VALUE.getValue(), metaObject);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时填充字段 - 数据更新时间
        this.setFieldValByNameIfNull("updateTime", LocalDateTime.now(), metaObject);
        // 更新时填充字段 - 更新人 ID
        this.setFieldValByNameIfNull("updateBy", DataSourcesDefaultValueEnum.UPDATE_BY_DEFAULT_VALUE.getValue(), metaObject);
    }

    /**
     * 如果字段为空，则设置字段值
     *
     * @param fieldName  字段名
     * @param fieldVal   字段值
     * @param metaObject 元对象
     */
    private void setFieldValByNameIfNull(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (Objects.isNull(metaObject.getValue(fieldName))) {
            this.setFieldValByName(fieldName, fieldVal, metaObject);
        }
    }
}
