package com.ranyk.vt.boot.example.satoken.domain.login.po;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的登录参数数据封装类
 * @date: 2026-03-01
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
public class AccountPO extends BasePO {

    @Serial
    private static final long serialVersionUID = 7385016344976078527L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码的 key
     */
    private String captchaKey;
    /**
     * 验证码
     */
    private String captcha;
}
