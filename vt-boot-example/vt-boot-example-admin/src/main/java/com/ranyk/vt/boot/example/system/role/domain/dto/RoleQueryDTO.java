package com.ranyk.vt.boot.example.system.role.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: RoleQueryDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色分页查询业务参数
 * @date: 2026-11-20
 */
@Data
public class RoleQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7721831550219816294L;

    /**
     * 当前页码
     */
    private Integer page = 1;
    /**
     * 每页条数
     */
    private Integer size = 10;
    /**
     * 角色名称, 模糊匹配
     */
    private String name;
    /**
     * 角色编码, 模糊匹配
     */
    private String code;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
}
