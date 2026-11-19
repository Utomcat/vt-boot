package com.ranyk.vt.boot.example.system.rbac.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.convert.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CLASS_NAME: SaPermissionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Sa-Token 权限数据源实现, 将 RBAC 解析出的角色与权限提供给 StpUtil 使用
 * @date: 2026-11-20
 */
@Component
@RequiredArgsConstructor
public class SaPermissionService implements StpInterface {

    /**
     * RBAC 权限解析服务
     */
    private final RbacService rbacService;

    /**
     * 返回指定账号拥有的权限码集合
     *
     * @param loginId   账号ID
     * @param loginType 账号体系标识
     * @return 权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Convert.toLong(loginId, null);
        return rbacService.permissions(userId);
    }

    /**
     * 返回指定账号拥有的角色编码集合
     *
     * @param loginId   账号ID
     * @param loginType 账号体系标识
     * @return 角色编码集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Convert.toLong(loginId, null);
        return rbacService.roleCodes(userId);
    }
}
