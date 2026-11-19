package com.ranyk.vt.boot.example.system.menu.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.base.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysMenu.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单与权限实体, 对应表 t_menu
 * @date: 2026-11-20
 */
@Data
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_menu")
public class SysMenu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -5568820477316615902L;

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
     * 路由地址, 目录与页面使用
     */
    private String path;
    /**
     * 前端组件标识, 页面使用, 如 system/user/UserList
     */
    private String component;
    /**
     * 图标名称
     */
    private String icon;
    /**
     * 权限码, 如 system:user:add
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
