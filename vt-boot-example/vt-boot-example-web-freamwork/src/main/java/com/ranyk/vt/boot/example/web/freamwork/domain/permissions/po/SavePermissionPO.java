package com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: SavePermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 新增权限请求参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 权限名称, 不能为空</li>
 *     <li>code: 权限编码, 不能为空</li>
 *     <li>type: 权限类型（1: 菜单; 2: 按钮; 3: 功能; 4: 其他;）, 不能为空</li>
 *     <li>remark: 描述, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record SavePermissionPO(@Nonnull String name, @Nonnull String code, @Nonnull Integer type, String remark) implements Serializable {

}
