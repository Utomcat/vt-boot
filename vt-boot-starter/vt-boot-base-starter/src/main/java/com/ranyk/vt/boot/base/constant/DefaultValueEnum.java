package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: DefaultValueEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 默认值枚举类
 * @date: 2026-03-17
 */
@Getter
public enum DefaultValueEnum {
    /**
     * 字符串默认值
     */
    STR_DEFAULT("字符串默认值", "-"),
    /**
     * 操作员默认值
     */
    OPERATOR_DEFAULT("操作员默认值", "0000000000000000001"),
    /**
     * 数据状态默认值
     */
    STATUS_DEFAULT("数据状态默认值", String.valueOf(DataStatusEnum.NORMAL.getValue())),
    /**
     * 租户 ID 默认值
     */
    TENANT_ID_DEFAULT("租户 ID 默认值", "0000000001"),
    /**
     * 性别默认值
     */
    SEX_DEFAULT("性别默认值", "3"),
    ;

    /**
     * 默认值描述
     */
    private final String desc;
    /**
     * 默认值
     */
    private final String value;

    /**
     * 构造函数 - 默认值枚举类
     *
     * @param desc 默认值描述
     * @param value 默认值
     */
    DefaultValueEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
