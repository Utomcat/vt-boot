package com.ranyk.vt.boot.example.satoken.domain.permissions.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * CLASS_NAME: PermissionDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 权限信息数据传输对象
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PermissionDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 8554734291817108225L;
    /**
     * 权限名称
     */
    private String name;
    /**
     * 权限编码
     */
    private String code;
    /**
     * 权限类型（1: 菜单; 2: 按钮; 3: 功能; 4: 其他;）
     */
    private Integer type;

    // 以下为扩展属性

    /**
     * 权限ID列表
     */
    private List<String> ids;

}
