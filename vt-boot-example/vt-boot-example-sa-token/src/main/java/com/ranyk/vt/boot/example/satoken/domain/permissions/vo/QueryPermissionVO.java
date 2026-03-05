package com.ranyk.vt.boot.example.satoken.domain.permissions.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QueryPermissionVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询权限信息返回前端视图参数对象 VO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class QueryPermissionVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1547460443330978954L;

    /**
     * 权限 ID
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
}
