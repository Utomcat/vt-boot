package com.ranyk.vt.boot.example.web.freamwork.domain.tenant.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: TenantDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户信息传输对象
 * @date: 2026-04-06
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TenantDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 6505140070960708315L;

    /**
     * 租户名称
     */
    private String name;
    /**
     * 租户编码
     */
    private String code;
}
