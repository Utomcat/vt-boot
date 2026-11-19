package com.ranyk.vt.boot.example.system.menu.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: MenuFormDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单新增/修改业务参数
 * @date: 2026-11-20
 */
@Data
public class MenuFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5301887746214403975L;

    /**
     * 菜单ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 父菜单ID, 为空或 0 表示顶级
     */
    private Long parentId;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 类型: DIR-目录 MENU-页面 BUTTON-按钮
     */
    private String type;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 前端组件标识
     */
    private String component;
    /**
     * 图标名称
     */
    private String icon;
    /**
     * 权限码
     */
    private String permission;
    /**
     * 排序值
     */
    private Integer sort;
    /**
     * 是否在侧边栏显示: 1-显示 0-隐藏
     */
    private Integer visible;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
}
