package com.ranyk.vt.boot.example.satoken.domain.role.po;

import com.ranyk.vt.boot.base.request.PageRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: QueryRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 查询角色参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QueryRolePO extends PageRequest {
    @Serial
    private static final long serialVersionUID = 2103688082154668167L;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    private Integer status;
    /**
     * 备注, 默认值为 -
     */
    private String remark;
}
