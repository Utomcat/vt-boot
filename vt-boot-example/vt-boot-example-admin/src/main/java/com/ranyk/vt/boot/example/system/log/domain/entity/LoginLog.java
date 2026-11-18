package com.ranyk.vt.boot.example.system.log.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: LoginLog.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录审计流水实体, 对应表 t_login_log
 * @date: 2026-11-16
 */
@Data
@TableName("t_login_log")
public class LoginLog implements Serializable {

    @Serial
    private static final long serialVersionUID = -1527743039011820771L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 用户ID(登录成功或匹配到账号时填充)
     */
    private Long userId;
    /**
     * 登录名(原始输入, 含未匹配账号)
     */
    private String username;
    /**
     * 登录方式: PASSWORD / REFRESH_TOKEN / API_KEY
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
