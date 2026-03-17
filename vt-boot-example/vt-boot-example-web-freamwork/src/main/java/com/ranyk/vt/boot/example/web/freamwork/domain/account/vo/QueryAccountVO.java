package com.ranyk.vt.boot.example.web.freamwork.domain.account.vo;

import jakarta.annotation.Nonnull;
import lombok.*;

import java.io.Serializable;

/**
 * CLASS_NAME: QueryAccountVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询账户信息返回前端数据封装类, 字段说明: <br/>
 * <ul>
 *     <li>id: 账户信息ID, 不能为空</li>
 *     <li>userName: 用户名, 不能为空</li>
 *     <li>remark: 备注</li>
 *     <li>status: 账户状态, 不能为空</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record QueryAccountVO(@Nonnull String id, @Nonnull String userName, String remark, @Nonnull Integer status) implements Serializable {
}
