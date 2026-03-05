package com.ranyk.vt.boot.base.context;

/**
 * CLASS_NAME: TenantContext.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户上下文监听类
 * @date: 2026-03-03
 */
public class TenantContext {
    /**
     * 租户ID 线程存储本地变量
     */
    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public static void setTenantId(String tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    public static String getTenantId() {
        return TENANT_ID.get();
    }

    /**
     * 清空租户ID
     */
    public static void clear() {
        TENANT_ID.remove();
    }
}
