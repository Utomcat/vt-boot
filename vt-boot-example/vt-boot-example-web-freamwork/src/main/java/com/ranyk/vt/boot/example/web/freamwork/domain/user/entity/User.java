package com.ranyk.vt.boot.example.web.freamwork.domain.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: User.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库用户信息表映射实体类
 * @date: 2026-03-17
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_info")
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 7484325061285576359L;

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
}
