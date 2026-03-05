package com.ranyk.vt.boot.example.satoken.domain.department.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: QueryDepartmentVO.java
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
public class QueryDepartmentVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6196457353366227800L;

    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门编码
     */
    private String code;
    /**
     * 父级部门ID
     */
    private String parentId;
    /**
     * 父级部门ID列表, 逗号分隔
     */
    private String parentIds;
    /**
     * 备注, 默认值为空字符串
     */
    private String remark;
}
