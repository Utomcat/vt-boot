package com.ranyk.vt.boot.example.satoken.domain.department.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除部门信息参数对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class DeleteDepartmentPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6592856060216126623L;
    /**
     * 部门ID
     */
    private String id;

    /**
     * 部门ID列表
     */
    private List<String> ids;
}
