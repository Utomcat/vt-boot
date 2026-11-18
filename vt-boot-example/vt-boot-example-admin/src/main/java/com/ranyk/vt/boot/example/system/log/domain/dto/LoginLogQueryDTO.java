package com.ranyk.vt.boot.example.system.log.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginLogQueryDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录日志分页查询参数
 * @date: 2026-11-16
 */
@Data
public class LoginLogQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6226857055431470333L;

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
     * 是否成功: 0-失败 1-成功
     */
    private Integer success;
}
