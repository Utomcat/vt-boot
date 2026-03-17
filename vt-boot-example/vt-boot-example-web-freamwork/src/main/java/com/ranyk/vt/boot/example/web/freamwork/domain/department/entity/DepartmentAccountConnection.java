package com.ranyk.vt.boot.example.web.freamwork.domain.department.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: DepartmentAccountConnection.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库部门账户关联关系表映射实体类
 * @date: 2026-03-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
@TableName("department_account_connection")
public class DepartmentAccountConnection extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -178737852989505398L;

    /**
     * 部门ID
     */
    private String departmentId;
    /**
     * 账户ID
     */
    private String accountId;
}
