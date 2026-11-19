package com.ranyk.vt.boot.example.system.role.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: SysRoleMenu.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色-菜单关联实体, 对应表 t_role_menu; 关联关系使用物理删除, 不继承公共审计字段
 * @date: 2026-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_role_menu")
public class SysRoleMenu implements Serializable {

    @Serial
    private static final long serialVersionUID = -8812406671923366145L;

    /**
     * 主键ID, 数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 构造角色-菜单关联, 用于新增授权
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    public SysRoleMenu(Long roleId, Long menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
