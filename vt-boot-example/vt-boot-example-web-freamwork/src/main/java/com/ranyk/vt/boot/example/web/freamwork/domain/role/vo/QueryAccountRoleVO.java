package com.ranyk.vt.boot.example.web.freamwork.domain.role.vo;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryAccountRoleVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询账户关联的角色信息返回前端 VO 视图类,字段说明: <br/>
 * <ul>
 *     <li>id: 角色 ID</li>
 *     <li>name: 角色名称</li>
 *     <li>code: 角色编码</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record QueryAccountRoleVO(String id, String name, String code) implements Serializable {
}
