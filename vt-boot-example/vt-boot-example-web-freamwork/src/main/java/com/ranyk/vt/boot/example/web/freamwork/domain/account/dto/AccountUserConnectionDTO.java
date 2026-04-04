package com.ranyk.vt.boot.example.web.freamwork.domain.account.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AccountUserConnectionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户关联关系数据传输对象
 * @date: 2026-03-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountUserConnectionDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 4156743720205092257L;

    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 用户ID
     */
    private String userId;

    // 以下为扩展属性字段

    /**
     * 用户名称
     */
    private String  userName;
    /**
     * 账户名称
     */
    private String  accountName;
}
