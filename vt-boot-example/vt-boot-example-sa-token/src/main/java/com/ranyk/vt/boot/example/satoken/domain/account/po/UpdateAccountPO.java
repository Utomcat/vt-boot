package com.ranyk.vt.boot.example.satoken.domain.account.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UpdateAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 修改账户信息数据封装对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateAccountPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3496082414719381654L;
    /**
     * 账户ID
     */
    private String id;
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
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    @Builder.Default
    private Integer status = 1;
}
