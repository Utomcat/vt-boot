package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: SessionKeyEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Sa-Token 会话属性键枚举
 * @date: 2026-11-16
 */
@Getter
public enum SessionKeyEnum {
    /**
     * 租户ID
     */
    TENANT_ID("租户ID", "tenantId"),
    /**
     * 用户名
     */
    USERNAME("用户名", "username"),
    /**
     * 真实姓名
     */
    REAL_NAME("真实姓名", "realName"),
    /**
     * 角色
     */
    ROLE("角色", "role"),
    ;

    private final String desc;
    private final String value;

    /**
     * 构造会话属性键
     *
     * @param desc 会话键的中文描述
     * @param value 写入 Sa-Token 会话时使用的键名
     */
    SessionKeyEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
