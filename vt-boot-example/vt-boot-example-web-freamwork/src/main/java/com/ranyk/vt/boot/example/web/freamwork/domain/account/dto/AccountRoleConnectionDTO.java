package com.ranyk.vt.boot.example.web.freamwork.domain.account.dto;

import cn.hutool.core.collection.CollUtil;
import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * 构建账户角色关系数据传输对象列表
     *
     * @return 账户角色关系数据传输对象列表
     */
    public List<AccountRoleConnectionDTO> buildAccountRoleConnectionDTOList() {
        if (CollUtil.isEmpty(this.accountIds) || CollUtil.isEmpty(this.roleIds)) {
            return Collections.emptyList();
        }
        List<AccountRoleConnectionDTO> accountRoleConnectionDTOList = new ArrayList<>(Math.max(this.roleIds.size(), this.accountIds.size()));
        this.accountIds.forEach(accountId -> this.roleIds.forEach(roleId -> accountRoleConnectionDTOList.add(AccountRoleConnectionDTO.builder().accountId(accountId).roleId(roleId).build())));
        return accountRoleConnectionDTOList;
    }
}
