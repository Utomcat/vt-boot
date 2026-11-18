package com.ranyk.vt.boot.example.system.log.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: LoginLogVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录日志返回结果
 * @date: 2026-11-16
 */
@Data
public class LoginLogVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3187766810450468773L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 登录名
     */
    private String username;
    /**
     * 登录方式
     */
    private String loginType;
    /**
     * 是否成功: 0-失败 1-成功
     */
    private Integer success;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 客户端IP
     */
    private String ip;
    /**
     * 设备信息
     */
    private String deviceInfo;
    /**
     * 登录时间
     */
    private LocalDateTime createTime;
}
