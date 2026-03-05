package com.ranyk.vt.boot.example.satoken.domain.permissions.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UpdatePermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 修改权限请求参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UpdatePermissionPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1992085017812352831L;

    /**
     * 权限ID
     */
    private String id;
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
     * 备注, 默认值为 -
     */
    private String remark;
}
