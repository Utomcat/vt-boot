package com.ranyk.vt.boot.datasource.constant;

import lombok.Getter;


/**
 * CLASS_NAME: DataSourcesDefaultValueEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库默认值枚举类
 * @date: 2026-02-13
 */
@Getter
public enum DataSourcesDefaultValueEnum {

    /**
     * 租户数据值默认值
     */
    TENANT_ID_DEFAULT_VALUE("租户数据值默认值", "0000000001"),
    /**
     * 数据状态默认值
     */
    STATUS_DEFAULT_VALUE("数据状态默认值", 1),
    /**
     * 创建人默认值
     */
    CREATE_BY_DEFAULT_VALUE("创建人默认值", "1"),
    /**
     * 更新人默认值
     */
    UPDATE_BY_DEFAULT_VALUE("更新人默认值", "1"),
    /**
     * 字符串默认值
     */
    STRING_VALUE_DEFAULT_VALUE("字符串默认值", "-");

    /**
     * 数据库默认值描述
     */
    private final String desc;
    /**
     * 数据库默认值
     */
    private final Object value;

    /**
     * 数据库默认值枚举类构造方法
     *
     * @param desc  数据库默认值描述
     * @param value 数据库默认值
     */
    DataSourcesDefaultValueEnum(String desc, Object value) {
        this.desc = desc;
        this.value = value;
    }
}
