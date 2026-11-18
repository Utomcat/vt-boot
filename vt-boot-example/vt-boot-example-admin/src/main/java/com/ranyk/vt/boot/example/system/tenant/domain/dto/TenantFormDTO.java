package com.ranyk.vt.boot.example.system.tenant.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: TenantFormDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户新增/修改参数
 * @date: 2026-11-16
 */
@Data
public class TenantFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1964151339649139441L;

    /**
     * 租户ID, 新增时为空, 修改时必填
     */
    private Long id;
    /**
     * 租户名称
     */
    private String name;
    /**
     * 租户编码(唯一标识)
     */
    private String code;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系邮箱
     */
    private String contactEmail;
    /**
     * 套餐: FREE / STANDARD / ENTERPRISE
     */
    private String plan;
    /**
     * 最大用户数配额
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
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 到期时间
     */
    private LocalDateTime expireTime;
    /**
     * 管理员备注
     */
    private String remark;
}
