package com.ranyk.vt.boot.example.web.freamwork.domain.role.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: RoleBundledPermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的角色绑定权限信息 PO 类, 字段说明:<br/>
 * <ul>
 *     <li>roleIds: 角色ID 列表</li>
 *     <li>permissionIds: 权限ID 列表</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record RoleBundledPermissionPO(List<String> roleIds, List<String> permissionIds) implements Serializable {
}
