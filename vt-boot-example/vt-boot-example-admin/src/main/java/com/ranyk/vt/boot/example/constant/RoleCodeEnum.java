package com.ranyk.vt.boot.example.constant;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * CLASS_NAME: RoleCodeEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色编码枚举, 与 t_user.role、t_role.code 取值保持一致
 * @date: 2026-11-20
 */
@Getter
public enum RoleCodeEnum {
    /**
     * 超级管理员, 拥有全部菜单与权限
     */
    ADMIN("超级管理员", "ADMIN"),
    /**
     * 普通用户, 仅可访问被授权的菜单
     */
    USER("普通用户", "USER"),
    ;

    /**
     * 角色描述
     */
    private final String desc;
    /**
     * 角色编码
     */
    private final String value;

    /**
     * 构造角色编码枚举
     *
     * @param desc  角色描述
     * @param value 角色编码
     */
    RoleCodeEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }

    /**
     * 根据角色编码获取枚举, 无法匹配时返回 null
     *
     * @param value 角色编码
     * @return 角色编码枚举
     */
    public static RoleCodeEnum getByValue(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (RoleCodeEnum item : values()) {
            if (StrUtil.equalsIgnoreCase(item.getValue(), value)) {
                return item;
            }
        }
        return null;
    }
}
