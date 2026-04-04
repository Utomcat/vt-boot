package com.ranyk.vt.boot.example.web.freamwork.domain.user.po;

import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: SaveUserPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 保存用户信息数据封装 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 用户名</li>
 *     <li>nickName: 昵称</li>
 *     <li>avatar: 头像地址</li>
 *     <li>sex: 性别: 1: 男; 2: 女; 3: 未知;</li>
 *     <li>email: 邮箱地址</li>
 *     <li>phone: 手机号码</li>
 *     <li>remark: 备注</li>
 * </ul>
 * @date: 2026-03-19
 */
@Builder
public record SaveUserPO(String name, String nickName, String avatar, Integer sex, String email, String phone, String remark) implements Serializable {
}
