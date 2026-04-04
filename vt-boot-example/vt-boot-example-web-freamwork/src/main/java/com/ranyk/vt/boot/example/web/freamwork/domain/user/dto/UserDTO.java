package com.ranyk.vt.boot.example.web.freamwork.domain.user.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: UserDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户数据传输对象
 * @date: 2026-03-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -5993288360765995170L;
    /**
     * 用户名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机
     */
    private String phone;

    // 以下为扩展字段属性

    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 账户用户关联关系数据ID
     */
    private String accountUserConnectionId;
}
