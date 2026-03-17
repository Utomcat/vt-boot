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
