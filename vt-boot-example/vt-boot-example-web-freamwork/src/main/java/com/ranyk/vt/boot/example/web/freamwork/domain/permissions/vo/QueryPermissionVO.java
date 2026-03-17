package com.ranyk.vt.boot.example.web.freamwork.domain.permissions.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryPermissionVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询权限信息返回前端视图参数对象 VO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 权限ID</li>
 *     <li>name: 权限名称</li>
 *     <li>code: 权限编码</li>
 *     <li>type: 权限类型（1: 菜单; 2: 按钮; 3: 功能; 4: 其他;）</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *     <li>remark: 描述, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryPermissionVO(@Nonnull String id, @Nonnull String name, @Nonnull String code, @Nonnull Integer type, @Nonnull Integer status, String remark) implements Serializable {

}
