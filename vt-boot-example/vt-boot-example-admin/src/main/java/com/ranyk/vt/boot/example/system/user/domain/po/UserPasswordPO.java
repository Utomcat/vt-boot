package com.ranyk.vt.boot.example.system.user.domain.po;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UserPasswordPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 重置用户密码接口入参, 使用请求体传递避免密码出现在 URL 查询串
 * @date: 2026-11-20
 */
@Data
public class UserPasswordPO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6621849173266011207L;

    /**
     * 新密码明文
     */
    private String newPassword;
}
