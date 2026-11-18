package com.ranyk.vt.boot.example.system.log.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginLogQueryPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录日志分页查询接口入参
 * @date: 2026-11-16
 */
@Data
public class LoginLogQueryPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7013945961373349472L;

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
