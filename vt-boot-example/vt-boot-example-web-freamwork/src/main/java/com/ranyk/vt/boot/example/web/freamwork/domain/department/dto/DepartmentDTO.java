package com.ranyk.vt.boot.example.web.freamwork.domain.department.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: DepartmentDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门信息数据传输对象
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DepartmentDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -6521354116261707868L;

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

    // 以下为扩展字段
}
