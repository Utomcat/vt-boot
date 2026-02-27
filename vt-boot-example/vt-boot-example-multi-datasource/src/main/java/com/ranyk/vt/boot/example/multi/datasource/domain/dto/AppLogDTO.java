package com.ranyk.vt.boot.example.multi.datasource.domain.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: AppLogVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 日志记录前端返回对象
 * @date: 2026-02-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppLogDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 6511603435692413861L;
    /**
     * 列1
     */
    private String columnOne;
    /**
     * 列2
     */
    private String columnTwo;
}
