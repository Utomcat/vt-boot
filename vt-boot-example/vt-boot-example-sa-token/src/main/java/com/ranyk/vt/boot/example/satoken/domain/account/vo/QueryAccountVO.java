package com.ranyk.vt.boot.example.satoken.domain.account.vo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QueryAccountVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询账户信息返回前端数据封装类
 * @date: 2026-03-05
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class QueryAccountVO  implements Serializable {
    @Serial
    private static final long serialVersionUID = -2243911885561311017L;
    /**
     * 主键ID 雪花算法生成
     */
    private String id;
    /**
     * 账户名
     */
    private String userName;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    private Integer status;
    /**
     * 备注, 默认值为空字符串
     */
    private String remark;
}
