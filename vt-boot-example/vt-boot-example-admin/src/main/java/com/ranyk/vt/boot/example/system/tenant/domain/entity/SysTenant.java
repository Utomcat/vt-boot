package com.ranyk.vt.boot.example.system.tenant.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.base.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: SysTenant.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户实体, 对应表 t_tenant
 * @date: 2026-11-16
 */
@Data
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_tenant")
public class SysTenant extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -3391021765044328771L;

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
     * 租户到期时间(NULL 表示永不过期)
     */
    private LocalDateTime expireTime;
    /**
     * 管理员备注
     */
    private String remark;
}
