package com.ranyk.vt.boot.example.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CLASS_NAME: RbacConstant.java
 *
 * @author ranyk
 * @version V1.0
 * @description: RBAC 相关的通用常量
 * @date: 2026-11-20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RbacConstant {

    /**
     * 通配权限码 - 持有该权限码即视为具备全部权限, 供超级管理员使用
     */
    public static final String SUPER_PERMISSION = "*";
    /**
     * 顶级菜单的父级 ID
     */
    public static final long ROOT_PARENT_ID = 0L;
    /**
     * 状态: 正常
     */
    public static final int STATUS_NORMAL = 1;
    /**
     * 内置角色/内置菜单标记: 是
     */
    public static final int BUILTIN_YES = 1;
    /**
     * 侧边栏可见: 是
     */
    public static final int VISIBLE_YES = 1;
    /**
     * 权限快照本地缓存有效期(秒), 角色/菜单变更时会主动失效
     */
    public static final long CACHE_TTL_SECONDS = 300L;
}
