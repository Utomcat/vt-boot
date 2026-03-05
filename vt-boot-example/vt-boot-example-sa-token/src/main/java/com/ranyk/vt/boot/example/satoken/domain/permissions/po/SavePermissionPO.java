package com.ranyk.vt.boot.example.satoken.domain.permissions.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: SavePermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 新增权限请求参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class SavePermissionPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8152040379943158929L;

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
     * 备注
     */
    private String remark;
}
