package com.ranyk.vt.boot.example.system.user.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UserQueryPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户分页查询接口入参
 * @date: 2026-11-16
 */
@Data
public class UserQueryPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3307253984410657881L;

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
