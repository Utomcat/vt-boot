package com.ranyk.vt.boot.example.system.menu.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CLASS_NAME: MenuVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单返回结果, 树形结构时通过 children 承载下级菜单
 * @date: 2026-11-20
 */
@Data
public class MenuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8803417629500441237L;

    /**
     * 菜单ID
     */
    private Long id;
    /**
     * 父菜单ID, 0 表示顶级
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
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 下级菜单
     */
    private List<MenuVO> children;
}
