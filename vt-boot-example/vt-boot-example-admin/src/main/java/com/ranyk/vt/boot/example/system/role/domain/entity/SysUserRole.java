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
 * CLASS_NAME: SysUserRole.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户-角色关联实体, 对应表 t_user_role; 关联关系使用物理删除, 不继承公共审计字段
 * @date: 2026-11-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_role")
public class SysUserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 3409115588622170334L;

    /**
     * 主键ID, 数据库自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 构造用户-角色关联, 用于新增绑定
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    public SysUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
