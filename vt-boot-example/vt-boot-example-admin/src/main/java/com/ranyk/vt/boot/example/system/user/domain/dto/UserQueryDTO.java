package com.ranyk.vt.boot.example.system.user.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UserQueryDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户分页查询参数
 * @date: 2026-11-16
 */
@Data
public class UserQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4148456946000771351L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 用户名, 模糊匹配
     */
    private String username;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 角色: ADMIN / USER
     */
    private String role;
}
