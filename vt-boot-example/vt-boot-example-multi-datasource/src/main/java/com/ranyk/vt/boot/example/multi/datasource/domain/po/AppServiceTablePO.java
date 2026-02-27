package com.ranyk.vt.boot.example.multi.datasource.domain.po;

import com.ranyk.vt.boot.base.domain.po.BasePO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: 业务记录前端返回对象.java
 *
 * @author ranyk
 * @version V1.0
 * @description:
 * @date: 2026-02-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppServiceTablePO extends BasePO {
    @Serial
    private static final long serialVersionUID = 4972468636889030363L;
    /**
     * 业务名称
     */
    private String serviceName;
    /**
     * 业务数据
     */
    private String serviceData;
}
