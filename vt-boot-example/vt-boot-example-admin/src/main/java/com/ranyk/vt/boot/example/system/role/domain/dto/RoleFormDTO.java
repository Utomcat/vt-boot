package com.ranyk.vt.boot.example.system.role.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: RoleFormDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色新增/修改业务参数
 * @date: 2026-11-20
 */
@Data
public class RoleFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3744021968851176201L;

    /**
     * 角色ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 排序值
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
