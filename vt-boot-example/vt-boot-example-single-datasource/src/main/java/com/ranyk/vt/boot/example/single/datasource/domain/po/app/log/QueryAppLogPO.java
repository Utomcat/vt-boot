package com.ranyk.vt.boot.example.single.datasource.domain.po.app.log;

import com.ranyk.vt.boot.base.domain.po.PageBasePO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: QueryAppLogPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 接收前端传入的日志记录查询条件数据封装类
 * @date: 2026-02-27
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper = true)
public class QueryAppLogPO extends PageBasePO {

    @Serial
    private static final long serialVersionUID = 1930443537492519746L;
    /**
     * 列1
     */
    private String columnOne;
    /**
     * 列2
     */
    private String columnTwo;
}
