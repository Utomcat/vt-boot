package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: DataStatusEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据状态枚举类
 * @date: 2026-03-06
 */
@Getter
public enum DataStatusEnum {

    /**
     * 正常/有效/其他正常状态
     */
    NORMAL("正常/有效/其他正常状态", 1),
    /**
     * 待启用
     */
    UN_ENABLE("待启用", 0),
    /**
     * 删除/停用/无效
     */
    DISABLE("删除/停用/无效", -1),
    /**
     * 其他非正常状态
     */
    OTHER("其他非正常状态", -2),
    ;

    /**
     * 数据状态描述
     */
    private final String desc;
    /**
     * 数据状态值
     */
    private final Integer value;

    /**
     * 构造方法
     *
     * @param desc  数据状态描述
     * @param value 数据状态值
     */
    DataStatusEnum(String desc, Integer value) {
        this.desc = desc;
        this.value = value;
    }
}
