package com.ranyk.vt.boot.example.web.freamwork.domain.operationRecord.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: OperationLogDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作日志数据传输 DTO 类
 * @date: 2026-03-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class OperationLogDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = -8655008920262064848L;

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
