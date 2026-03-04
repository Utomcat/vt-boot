package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

/**
 * CLASS_NAME: TenantEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户枚举类
 * @date: 2026-03-03
 */
@Getter
public enum TenantEnum {
    /**
     * 租户ID
     */
    TENANT_ID("租户ID", "X-Tenant-Id");

    /**
     * 描述
     */
    private final String desc;
    /**
     * 值
     */
    private final String value;

    /**
     * 构造函数 - 描述、值
     *
     * @param desc 描述
     * @param value 值
     */
    TenantEnum(String desc, String value) {
        this.desc = desc;
        this.value = value;
    }
}
