package com.ranyk.vt.boot.example.satoken.domain.role.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteRolePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除角色信息参数对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class DeleteRolePO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4466965118355959581L;

    /**
     * 角色ID
     */
    private String id;
    /**
     * 角色ID列表
     */
    private List<String> ids;
}
