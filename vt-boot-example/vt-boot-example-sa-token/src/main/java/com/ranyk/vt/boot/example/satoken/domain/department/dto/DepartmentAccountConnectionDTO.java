package com.ranyk.vt.boot.example.satoken.domain.department.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * CLASS_NAME: DepartmentAccountConnectionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门账户关联关系数据传输对象
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DepartmentAccountConnectionDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 8825923256947795625L;

    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 账户ID
     */
    private String accountId;

    // 以下为扩展字段属性

    /**
     * 部门和账户关联关系的数据 ID List 集合
     */
    private List<String> ids;
    /**
     * 部门ID List 集合
     */
    private List<String> departmentIds;
    /**
     * 账户ID List 集合
     */
    private List<String> accountIds;
}
