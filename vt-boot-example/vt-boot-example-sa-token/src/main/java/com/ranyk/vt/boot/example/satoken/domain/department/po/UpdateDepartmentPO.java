package com.ranyk.vt.boot.example.satoken.domain.department.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UpdateDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 更新部门信息参数对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateDepartmentPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1166052541556616742L;
    /**
     * 部门ID
     */
    private String id;
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
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    @Builder.Default
    private Integer status = 1;
    /**
     * 备注, 默认值为空字符串
     */
    private String remark;
}
