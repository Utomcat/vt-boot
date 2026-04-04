package com.ranyk.vt.boot.example.web.freamwork.domain.user.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryUserVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息查询结果封装视图 VO 对象, 字段说明: <br/>
 * <ul>
 *     <li>id: 用户ID, 不能为空</li>
 *     <li>name: 用户名</li>
 *     <li>sex: 性别</li>
 *     <li>avatar: 头像</li>
 *     <li>nickName: 昵称</li>
 *     <li>phone: 手机</li>
 *     <li>email: 邮箱</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *     <li>remark: 备注</li>
 * </ul>
 * @date: 2026-03-17
 */
@Builder
public record QueryUserVO(@Nonnull String id, String name, Integer sex, String avatar, String nickName, String phone, String email, Integer status, String remark) implements Serializable {
}
