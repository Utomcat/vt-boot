package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: UpdateUserPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 更新用户信息数据封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>id: 用户ID, 不能为空</li>
 *     <li>name: 用户名</li>
 *     <li>nickName: 昵称</li>
 *     <li>avatar: 头像地址</li>
 *     <li>sex: 性别: 1: 男; 2: 女; 3: 未知</li>
 *     <li>email: 邮箱</li>
 *     <li>phone: 手机</li>
 * </ul>
 * @date: 2026-03-19
 */
@Builder
public record UpdateUserPO(@Nonnull String id, String name, String nickName, String avatar, Integer sex, String email, String phone, Integer status, String remark) implements Serializable {
}
