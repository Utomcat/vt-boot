package com.ranyk.vt.boot.example.web.freamwork.domain.role.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: RoleDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色信息数据传输对象
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 8248816374169922544L;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String code;

    // 以下为扩展字段
}
