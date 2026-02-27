package com.ranyk.vt.boot.example.single.datasource.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AppLog.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志数据表映射对象
 * @date: 2026-02-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("app_log")
@EqualsAndHashCode(callSuper = true)
public class AppLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 4702056900180331481L;
    /**
     * 列1
     */
    private String columnOne;
    /**
     * 列2
     */
    private String columnTwo;
}
