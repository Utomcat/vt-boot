package com.ranyk.vt.boot.datasource.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ranyk.vt.boot.base.constant.AutoFillFieldEnum;
import org.apache.ibatis.reflection.MetaObject;

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
        AutoFillFieldEnum.getInsertAutoFillFieldList().forEach(fillFieldEnum -> this.setFieldValByNameIfNull(fillFieldEnum.getFieldName(), fillFieldEnum.getDefaultValue(), metaObject));
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        AutoFillFieldEnum.getUpdateAutoFillFieldList().forEach(fillFieldEnum -> this.setFieldValByNameIfNull(fillFieldEnum.getFieldName(), fillFieldEnum.getDefaultValue(), metaObject));
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
