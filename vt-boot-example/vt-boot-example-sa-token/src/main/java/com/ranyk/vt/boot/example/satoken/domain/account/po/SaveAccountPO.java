package com.ranyk.vt.boot.example.satoken.domain.account.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: SaveAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 新增账户信息参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class SaveAccountPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2436455897754416943L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 备注
     */
    private String remark;
}
