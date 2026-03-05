package com.ranyk.vt.boot.example.satoken.domain.account.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QueryAccountPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询账户信息参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class QueryAccountPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7524597764270527235L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 备注
     */
    private String remark;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    @Builder.Default
    private Integer status = 1;
    /**
     * 当前页码
     */
    @Builder.Default
    private Integer currentPage = 1;
    /**
     * 每页显示数量
     */
    @Builder.Default
    private Integer pageSize = 10;
}
