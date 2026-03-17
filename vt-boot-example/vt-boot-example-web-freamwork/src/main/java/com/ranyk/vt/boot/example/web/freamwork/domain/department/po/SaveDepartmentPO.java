package com.ranyk.vt.boot.example.web.freamwork.domain.department.po;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: SaveDepartmentPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门信息保存对象 PO 类, 字段说明: <br/>
 * <ul>
 *     <li>name: 部门名称</li>
 *     <li>code: 部门编码</li>
 *     <li>parentId: 父级部门ID</li>
 *     <li>parentIds: 父级部门ID列表, 逗号分隔</li>
 *     <li>remark: 备注, 默认值为空字符串</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record SaveDepartmentPO(@Nonnull String name, @Nonnull String code, String parentId, String parentIds, String remark) implements Serializable {

    public SaveDepartmentPO {
        if (StrUtil.isBlank(parentId)) {
            parentId = "-";
        }
        if (StrUtil.isBlank(parentIds)) {
            parentIds = "-";
        }
    }
}
