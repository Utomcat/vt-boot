package com.ranyk.vt.boot.example.web.freamwork.domain.account.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: UpdateAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 修改账户信息数据封装对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 账户信息ID, 不能为空</li>
 *     <li>userName: 用户名</li>
 *     <li>password: 密码</li>
 *     <li>remark: 账户备注</li>
 *     <li>status: 账户状态, 默认为 1</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record UpdateAccountPO(@Nonnull String id, String userName, String password, String remark, Integer status) implements Serializable {

    public UpdateAccountPO {
        if (Objects.isNull(status)) {
            status = 1;
        }
    }

}
