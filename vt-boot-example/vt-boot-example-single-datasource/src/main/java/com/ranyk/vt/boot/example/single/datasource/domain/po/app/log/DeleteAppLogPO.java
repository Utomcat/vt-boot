package com.ranyk.vt.boot.example.single.datasource.domain.po.app.log;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: DeleteAppLogPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 接收前端传入的删除业务日志数据封装对象
 * @date: 2026-02-27
 */
@Data
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DeleteAppLogPO extends BasePO {

    @Serial
    private static final long serialVersionUID = 6613418251142410267L;
}
