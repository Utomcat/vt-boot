package com.ranyk.vt.boot.example.system.role.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: RoleQueryPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色分页查询接口入参
 * @date: 2026-11-20
 */
@Data
public class RoleQueryPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6640318821799254841L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 角色名称, 模糊匹配
     */
    private String name;
    /**
     * 角色编码, 模糊匹配
     */
    private String code;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
}
