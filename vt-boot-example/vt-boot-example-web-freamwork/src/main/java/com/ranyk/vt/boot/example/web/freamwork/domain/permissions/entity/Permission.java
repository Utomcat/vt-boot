package com.ranyk.vt.boot.example.web.freamwork.domain.permissions.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: Permission.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库权限信息表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@TableName("permission_info")
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -1586365478036054931L;

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

}
