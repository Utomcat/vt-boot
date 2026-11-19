package com.ranyk.vt.boot.example.system.menu.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: MenuFormPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单新增/修改接口入参
 * @date: 2026-11-20
 */
@Data
public class MenuFormPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1472039851694407823L;

    /**
     * 菜单ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 父菜单ID, 为空或 0 表示顶级
     */
    private Long parentId;
    /**
     * 菜单名称, 必填
     */
    private String name;
    /**
     * 类型: DIR-目录 MENU-页面 BUTTON-按钮, 必填
     */
    private String type;
    /**
     * 路由地址, 目录与页面必填
     */
    private String path;
    /**
     * 前端组件标识, 页面必填, 如 system/user/UserList
     */
    private String component;
    /**
     * 图标名称
     */
    private String icon;
    /**
     * 权限码, 页面与按钮使用, 如 system:user:add
     */
    private String permission;
    /**
     * 排序值, 越小越靠前
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
