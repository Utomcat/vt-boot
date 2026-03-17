package com.ranyk.vt.boot.example.web.freamwork.domain.role.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除角色信息参数对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 角色ID</li>
 *     <li>ids: 角色ID列表</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record DeleteRolePO(String id, List<String> ids) implements Serializable {

}
