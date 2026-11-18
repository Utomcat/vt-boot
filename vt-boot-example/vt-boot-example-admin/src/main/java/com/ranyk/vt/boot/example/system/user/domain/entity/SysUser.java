package com.ranyk.vt.boot.example.system.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.base.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: SysUser.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户实体, 对应表 t_user
 * @date: 2026-11-16
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_user")
public class SysUser extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8127356124500080911L;

    /**
     * 登录名
     */
    private String username;
    /**
     * 加密密码(BCrypt)
     */
    private String password;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像相对路径
     */
    private String avatar;
    /**
     * 角色: ADMIN / USER
     */
    private String role;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 连续登录失败次数
     */
    private Integer loginFailCount;
    /**
     * 账号锁定截止时间
     */
    private LocalDateTime lockTime;
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    /**
     * 管理员备注
     */
    private String remark;
}
