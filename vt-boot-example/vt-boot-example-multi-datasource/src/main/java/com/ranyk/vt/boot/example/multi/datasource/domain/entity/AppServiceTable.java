package com.ranyk.vt.boot.example.multi.datasource.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AppServiceTable.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务测试表
 * @date: 2026-02-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("app_service_table")
@EqualsAndHashCode(callSuper = true)
public class AppServiceTable extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1447660024375069900L;
    /**
     * 业务名称
     */
    private String serviceName;
    /**
     * 业务数据
     */
    private String serviceData;
}
