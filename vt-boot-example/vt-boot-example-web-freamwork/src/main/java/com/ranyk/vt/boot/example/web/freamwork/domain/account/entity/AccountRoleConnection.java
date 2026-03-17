package com.ranyk.vt.boot.example.web.freamwork.domain.account.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AccountRoleConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库账户角色关联关系表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
@TableName("account_role_connection")
public class AccountRoleConnection extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1584162616470454203L;

    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 角色ID
     */
    private String roleId;
}
