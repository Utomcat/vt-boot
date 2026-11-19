package com.ranyk.vt.boot.example.system.role.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CLASS_NAME: RoleVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色返回结果
 * @date: 2026-11-20
 */
@Data
public class RoleVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2955880314477201964L;

    /**
     * 角色ID
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
     * 是否内置角色: 1-是 0-否
     */
    private Integer builtin;
    /**
     * 排序值
     */
    private Integer sort;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 已授权的菜单ID集合, 仅详情接口返回
     */
    private List<Long> menuIds;
}
