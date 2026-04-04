package com.ranyk.vt.boot.base.constant;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static com.ranyk.vt.boot.base.constant.DefaultValueEnum.*;

/**
 * CLASS_NAME: AutoFillFieldEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自动填充字段名枚举
 * @date: 2026-03-04
 */
@Getter
public enum AutoFillFieldEnum {
    /**
     * 数据创建时间
     */
    CREATE_TIME("createTime", "数据创建时间", LocalDateTime.now()),
    /**
     * 数据更新时间
     */
    UPDATE_TIME("updateTime", "数据更新时间", LocalDateTime.now()),
    /**
     * 创建人 ID
     */
    CREATE_BY("createBy", "创建人 ID", OPERATOR_DEFAULT.getValue()),
    /**
     * 更新人 ID
     */
    UPDATE_BY("updateBy", "更新人 ID", OPERATOR_DEFAULT.getValue()),
    /**
     * 备注
     */
    REMARK("remark", "备注", STR_DEFAULT.getValue()),
    /**
     * 数据状态
     */
    STATUS("status", "数据状态", Integer.parseInt(STATUS_DEFAULT.getValue())),
    /**
     * 租户 ID
     */
    TENANT_ID("tenantId", "租户 ID", TENANT_ID_DEFAULT.getValue());

    /**
     * 字段名
     */
    private final String fieldName;
    /**
     * 字段描述
     */
    private final String desc;
    /**
     * 字段默认值
     */
    private final Object defaultValue;

    /**
     * 构造方法
     *
     * @param fieldName    字段名
     * @param desc         字段描述
     * @param defaultValue 字段默认值
     */
    AutoFillFieldEnum(String fieldName, String desc, Object defaultValue) {
        this.fieldName = fieldName;
        this.desc = desc;
        this.defaultValue = defaultValue;
    }

    /**
     * 获取插入数据时自动填充字段列表
     *
     * @return 插入数据时自动填充字段列表
     */
    public static List<AutoFillFieldEnum> getInsertAutoFillFieldList() {
        return List.of(AutoFillFieldEnum.values());
    }

    /**
     * 获取更新数据时自动填充字段列表
     *
     * @return 更新数据时自动填充字段列表
     */
    public static List<AutoFillFieldEnum> getUpdateAutoFillFieldList() {
        return List.of(UPDATE_TIME, UPDATE_BY);
    }
}
