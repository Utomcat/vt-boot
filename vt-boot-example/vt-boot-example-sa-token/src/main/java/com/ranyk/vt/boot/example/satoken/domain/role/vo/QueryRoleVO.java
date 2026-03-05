package com.ranyk.vt.boot.example.satoken.domain.role.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QueryRoleVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询角色返回前端视图数据参数封装 VO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class QueryRoleVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3886322589773149399L;

    /**
     * 角色 ID
     */
    private String id;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
}
