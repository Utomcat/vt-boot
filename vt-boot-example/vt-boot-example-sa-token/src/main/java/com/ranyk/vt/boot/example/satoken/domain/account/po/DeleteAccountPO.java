package com.ranyk.vt.boot.example.satoken.domain.account.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除账户信息参数封装对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class DeleteAccountPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1970067102399019789L;
    /**
     * 账户ID
     */
    private String id;
    /**
     * 账户ID列表
     */
    private List<String> ids;
}
