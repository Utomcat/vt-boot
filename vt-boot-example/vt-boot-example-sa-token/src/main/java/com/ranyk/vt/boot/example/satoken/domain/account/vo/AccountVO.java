package com.ranyk.vt.boot.example.satoken.domain.account.vo;

import com.ranyk.vt.boot.base.domain.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AccountVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户登录信息返回前端数据封装类
 * @date: 2026-03-01
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
public class AccountVO extends BaseVO {
    @Serial
    private static final long serialVersionUID = 6705372948383724394L;
    /**
     * 登录成功返回的 token 数据值
     */
    private String token;
}
