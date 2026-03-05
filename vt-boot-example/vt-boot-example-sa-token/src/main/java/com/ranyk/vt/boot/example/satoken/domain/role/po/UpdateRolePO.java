package com.ranyk.vt.boot.example.satoken.domain.role.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UpdateRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 修改角色请求参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateRolePO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1421497577285192131L;

    /**
     * 角色ID
     */
    private String id;
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
     * 备注
     */
    private String remark;
}
