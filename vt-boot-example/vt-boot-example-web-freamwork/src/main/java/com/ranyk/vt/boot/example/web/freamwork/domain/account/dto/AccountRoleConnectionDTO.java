package com.ranyk.vt.boot.example.web.freamwork.domain.account.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * CLASS_NAME: AccountRoleConnectionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户角色关联关系数据传输对象
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountRoleConnectionDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -6629176301733495648L;

    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 角色ID
     */
    private String roleId;

    // 以下为扩展属性

    /**
     * 角色ID 集合
     */
    private List<String> roleIds;
    /**
     * 账户ID 集合
     */
    private List<String> accountIds;
}
