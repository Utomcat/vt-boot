package com.ranyk.vt.boot.example.web.freamwork.domain.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AccountUserConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库账户用户信息关联关系表映射实体类
 * @date: 2026-03-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
@TableName("account_user_connection")
public class AccountUserConnection extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 2361514349039763416L;

    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 用户ID
     */
    private String userId;
}
