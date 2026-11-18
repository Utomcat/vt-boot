package com.ranyk.vt.boot.example.system.tenant.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: TenantQueryDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户分页查询参数
 * @date: 2026-11-16
 */
@Data
public class TenantQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7881869108488851511L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 租户名称, 模糊匹配
     */
    private String name;
    /**
     * 租户编码, 模糊匹配
     */
    private String code;
    /**
     * 套餐
     */
    private String plan;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
}
