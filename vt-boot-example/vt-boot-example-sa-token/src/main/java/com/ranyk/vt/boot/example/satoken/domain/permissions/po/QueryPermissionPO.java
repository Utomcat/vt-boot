package com.ranyk.vt.boot.example.satoken.domain.permissions.po;

import com.ranyk.vt.boot.base.request.PageRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: QueryPermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询权限参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QueryPermissionPO extends PageRequest {

    @Serial
    private static final long serialVersionUID = -7587458692715926399L;

    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限编码
     */
    private String code;
    /**
     * 权限类型（1: 菜单; 2: 按钮; 3: 功能; 4: 其他;）
     */
    private Integer type;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    private Integer status;
    /**
     * 备注, 默认值为空字符串
     */
    private String remark;

}
