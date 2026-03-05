package com.ranyk.vt.boot.example.satoken.domain.role.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: SaveRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 新增角色请求参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class SaveRolePO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7820745390021355201L;

    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 备注
     */
    private String remark;
}
