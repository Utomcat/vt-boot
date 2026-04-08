package com.ranyk.vt.boot.example.web.freamwork.domain.permissions.vo;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryAccountPermissionVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询账户关联的权限信息返回前端 VO 视图类,字段说明: <br/>
 * <ul>
 *     <li>id: 权限ID</li>
 *     <li>name: 权限名称</li>
 *     <li>code: 权限码</li>
 * </ul>
 * @date: 2026-04-08
 */
@Builder
public record QueryAccountPermissionVO(String id, String name, String code) implements Serializable {
}
