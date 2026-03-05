package com.ranyk.vt.boot.example.satoken.domain.department.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: SaveDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门信息保存对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class SaveDepartmentPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1759510399108912218L;

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
