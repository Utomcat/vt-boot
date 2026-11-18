package com.ranyk.vt.boot.example.system.tenant.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: TenantVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户信息返回结果
 * @date: 2026-11-16
 */
@Data
public class TenantVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4470882304592211233L;

    /**
     * 租户ID
     */
    private Long id;
    /**
     * 租户名称
     */
    private String name;
    /**
     * 租户编码
     */
    private String code;
    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系邮箱
     */
    private String contactEmail;
    /**
     * 套餐
     */
    private String plan;
    /**
     * 最大用户数
     */
    private Integer maxUsers;
    /**
     * 最大存储空间(MB)
     */
    private Long maxStorageMb;
    /**
     * 每日Token上限
     */
    private Long maxDailyTokens;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 到期时间
     */
    private LocalDateTime expireTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 备注
     */
    private String remark;
}
