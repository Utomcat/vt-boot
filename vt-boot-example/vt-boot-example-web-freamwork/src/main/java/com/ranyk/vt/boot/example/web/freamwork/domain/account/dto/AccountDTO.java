package com.ranyk.vt.boot.example.web.freamwork.domain.account.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AccountDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录信息数据传输类
 * @date: 2026-03-01
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -1577378271785876221L;

    // 以下为账户信息数据表字段

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;

    // 以下为账户登录参数字段

    /**
     * 验证码的 key
     */
    private String captchaKey;
    /**
     * 验证码
     */
    private String captcha;

    /**
     * 登录成功返回的 token 数据值
     */
    private String token;

    /**
     * 部门ID
     */
    private String departmentId;
}
