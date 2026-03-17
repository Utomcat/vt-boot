package com.ranyk.vt.boot.example.web.freamwork.domain.account.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: Account.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户数据库映射实体类
 * @date: 2026-03-01
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@TableName("account_info")
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8984559218921264930L;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 租户ID
     */
    @TableField("tenant_id")
    private String tenantId;
}
