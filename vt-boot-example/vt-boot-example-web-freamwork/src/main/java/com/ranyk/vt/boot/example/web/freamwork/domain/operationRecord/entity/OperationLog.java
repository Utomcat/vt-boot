package com.ranyk.vt.boot.example.web.freamwork.domain.operationRecord.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.datasource.domain.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: OperationLog.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作日志表映射实体类
 * @date: 2026-03-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@TableName("operation_log_info")
@EqualsAndHashCode(callSuper = true)
public class OperationLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -524367631444053233L;

    /**
     * 操作名称
     */
    private String name;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作参数
     */
    private String param;
    /**
     * 操作人
     */
    private String operator;
}
