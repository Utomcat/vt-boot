package com.ranyk.vt.boot.example.system.user.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UserFormDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户新增/修改参数
 * @date: 2026-11-16
 */
@Data
public class UserFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8300544871029317481L;

    /**
     * 用户ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 登录名, 新增时必填
     */
    private String username;
    /**
     * 密码, 新增时必填, 修改时为空表示不修改
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
     * 角色: ADMIN / USER
     */
    private String role;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 管理员备注
     */
    private String remark;
}
