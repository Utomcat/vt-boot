package com.ranyk.vt.boot.example.system.token.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: UserRefreshToken.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户刷新令牌实体, 对应表 t_user_refresh_token
 * @date: 2026-11-16
 */
@Data
@TableName("t_user_refresh_token")
public class UserRefreshToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 6311941922105983175L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * Refresh Token 值
     */
    private String refreshToken;
    /**
     * 设备信息
     */
    private String deviceInfo;
    /**
     * 创建时客户端IP
     */
    private String ip;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    /**
     * 是否已撤销: 0-有效 1-已撤销
     */
    private Integer revoked;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
