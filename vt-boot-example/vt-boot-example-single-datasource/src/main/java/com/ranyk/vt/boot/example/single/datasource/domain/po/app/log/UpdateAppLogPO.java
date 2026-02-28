package com.ranyk.vt.boot.example.single.datasource.domain.po.app.log;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: UpdateAppLogPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 接收前端传入的更新业务日志记录数据类
 * @date: 2026-02-27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateAppLogPO extends BasePO {

    @Serial
    private static final long serialVersionUID = -2644695838397065878L;
    /**
     * 列1
     */
    private String columnOne;
    /**
     * 列2
     */
    private String columnTwo;
}
