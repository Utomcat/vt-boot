package com.ranyk.vt.boot.example.web.freamwork.domain.role.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryRoleVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询角色返回前端视图数据参数封装 VO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 角色ID</li>
 *     <li>name: 角色名称</li>
 *     <li>code: 角色编码</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *     <li>remark: 描述</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryRoleVO(@Nonnull String id, String name, String code, Integer status, String remark) implements Serializable {

}
