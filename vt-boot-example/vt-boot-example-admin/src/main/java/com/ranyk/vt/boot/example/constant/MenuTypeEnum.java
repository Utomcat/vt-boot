package com.ranyk.vt.boot.example.constant;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * CLASS_NAME: MenuTypeEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单类型枚举, 对应 t_menu.type
 * @date: 2026-11-20
 */
@Getter
public enum MenuTypeEnum {
    /**
     * 目录, 仅用于分组, 不对应前端页面
     */
    DIR("目录", "DIR"),
    /**
     * 页面, 对应前端一个路由与组件
     */
    MENU("页面", "MENU"),
    /**
     * 按钮, 不对应路由, 仅提供权限码
     */
    BUTTON("按钮", "BUTTON"),
    ;

    /**
     * 类型描述
     */
    private final String desc;
    /**
     * 类型编码
     */
    private final String value;

    /**
     * 构造菜单类型枚举
     *
     * @param desc  类型描述
     * @param value 类型编码
     */
    MenuTypeEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }

    /**
     * 根据类型编码获取枚举, 无法匹配时返回 null
     *
     * @param value 类型编码
     * @return 菜单类型枚举
     */
    public static MenuTypeEnum getByValue(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (MenuTypeEnum item : values()) {
            if (StrUtil.equalsIgnoreCase(item.getValue(), value)) {
                return item;
            }
        }
        return null;
    }
}
