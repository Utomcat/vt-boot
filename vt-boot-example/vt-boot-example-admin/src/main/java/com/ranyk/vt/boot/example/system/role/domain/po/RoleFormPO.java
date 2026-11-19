package com.ranyk.vt.boot.example.system.role.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: RoleFormPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色新增/修改接口入参
 * @date: 2026-11-20
 */
@Data
public class RoleFormPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2214503906128873471L;

    /**
     * 角色ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 角色编码, 新增时必填, 内置角色的编码不可修改
     */
    private String code;
    /**
     * 角色名称, 新增时必填
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 排序值, 越小越靠前
     */
    private Integer sort;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 授权的菜单ID集合, 为空表示不调整授权
     */
    private List<Long> menuIds;
}
