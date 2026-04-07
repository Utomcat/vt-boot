package com.ranyk.vt.boot.example.web.freamwork.domain.tenant.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: Tenant.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户信息表映射实体类
 * @date: 2026-04-06
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tenant_info")
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class Tenant extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4152190741364951334L;

    /**
     * 租户名称
     */
    private String name;
    /**
     * 租户编码
     */
    private String code;

}
