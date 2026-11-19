package com.ranyk.vt.boot.example.auth.domain.vo;

import com.ranyk.vt.boot.example.system.menu.domain.vo.MenuVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: UserMenuVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 当前登录用户的角色、权限与可见菜单树
 * @date: 2026-11-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMenuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7120345591804473821L;

    /**
     * 角色编码集合
     */
    private List<String> roles;
    /**
     * 权限码集合, 超级管理员为 ["*"]
     */
    private List<String> permissions;
    /**
     * 可见菜单树(仅目录与页面)
     */
    private List<MenuVO> menus;
}
