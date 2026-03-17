package com.ranyk.vt.boot.example.web.freamwork.domain.permissions.po;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeletePermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除权限信息参数对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 权限ID</li>
 *     <li>ids: 权限ID列表</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record DeletePermissionPO(String id, List<String> ids) implements Serializable {

}
