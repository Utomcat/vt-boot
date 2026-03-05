package com.ranyk.vt.boot.example.satoken.domain.department.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QueryDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description:
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class QueryDepartmentPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4846807490409790859L;

    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门编码
     */
    private String code;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    private Integer status;
    /**
     * 备注, 默认值为空字符串
     */
    private String remark;
}
