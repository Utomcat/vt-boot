package com.ranyk.vt.boot.example.web.freamwork.domain.role.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: SaveRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 新增角色请求参数封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 角色名称, 不能为空</li>
 *     <li>code: 角色编码, 不能为空</li>
 *     <li>remark: 描述, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record SaveRolePO(@Nonnull String name, @Nonnull String code, String remark) implements Serializable {

}
