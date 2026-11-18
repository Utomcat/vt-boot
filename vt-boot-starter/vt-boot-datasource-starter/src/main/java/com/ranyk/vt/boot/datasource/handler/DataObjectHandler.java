package com.ranyk.vt.boot.datasource.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ranyk.vt.boot.base.constant.AutoFillFieldEnum;
import com.ranyk.vt.boot.base.constant.DefaultValueEnum;
import com.ranyk.vt.boot.base.context.TenantContext;
import com.ranyk.vt.boot.base.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * CLASS_NAME: DataObjectHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库对象监听实现类, 在插入和修改时对公共字段进行填充, 如创建时间、更新时间、租户ID、
 * 逻辑删除标记、创建人、更新人等字段, 以保证数据的完整性和一致性。
 * <p>
 * 填充规则:
 * <ol>
 *     <li>租户ID 优先取 {@link TenantContext} 中的值, 未取到时使用默认租户ID</li>
 *     <li>创建人/更新人 优先取 {@link UserContext} 中的值, 未取到时使用默认操作人ID</li>
 *     <li>仅对实体中实际存在的字段(setter 存在)生效, 已赋值的字段不覆盖</li>
 * </ol>
 * </p>
 * @date: 2026-02-13
 */
@Slf4j
public class DataObjectHandler implements MetaObjectHandler {

    /**
     * 创建时间字段名
     */
    private static final String FIELD_CREATE_TIME = "createTime";
    /**
     * 更新时间字段名
     */
    private static final String FIELD_UPDATE_TIME = "updateTime";
    /**
     * 租户ID字段名
     */
    private static final String FIELD_TENANT_ID = "tenantId";
    /**
     * 逻辑删除标记字段名
     */
    private static final String FIELD_DELETED = "deleted";
    /**
     * 创建人字段名
     */
    private static final String FIELD_CREATE_BY = "createBy";
    /**
     * 更新人字段名
     */
    private static final String FIELD_UPDATE_BY = "updateBy";
    /**
     * 逻辑删除标记默认值 - 未删除
     */
    private static final Integer NOT_DELETED = 0;

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        fillFieldValByNameIfNull(FIELD_CREATE_TIME, now, metaObject);
        fillFieldValByNameIfNull(FIELD_UPDATE_TIME, now, metaObject);
        fillFieldValByNameIfNull(FIELD_TENANT_ID, resolveTenantId(), metaObject);
        fillFieldValByNameIfNull(FIELD_DELETED, NOT_DELETED, metaObject);
        Long operatorId = resolveOperatorId();
        fillFieldValByNameIfNull(FIELD_CREATE_BY, operatorId, metaObject);
        fillFieldValByNameIfNull(FIELD_UPDATE_BY, operatorId, metaObject);
        // 兼容部分实体中额外声明的备注、状态字段, 使用枚举中的默认值填充
        fillDefaultFieldIfNull(metaObject, AutoFillFieldEnum.REMARK);
        fillDefaultFieldIfNull(metaObject, AutoFillFieldEnum.STATUS);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间每次更新都要刷新, 因此直接覆盖
        setIfHasSetter(FIELD_UPDATE_TIME, LocalDateTime.now(), metaObject);
        setIfHasSetter(FIELD_UPDATE_BY, resolveOperatorId(), metaObject);
    }

    /**
     * 使用枚举定义的默认值填充字段 - 当实体存在该字段且值为空时填充
     *
     * @param metaObject   元对象
     * @param fillFieldEnum 自动填充字段枚举
     */
    private void fillDefaultFieldIfNull(MetaObject metaObject, AutoFillFieldEnum fillFieldEnum) {
        fillFieldValByNameIfNull(fillFieldEnum.getFieldName(), fillFieldEnum.getDefaultValue(), metaObject);
    }

    /**
     * 如果字段存在且为空，则设置字段值
     *
     * @param fieldName  字段名
     * @param fieldVal   字段值
     * @param metaObject 元对象
     */
    private void fillFieldValByNameIfNull(String fieldName, Object fieldVal, MetaObject metaObject) {
        // 实体中不存在该字段时跳过填充, 以兼容统一基类后缺失的业务字段(如 status、remark)
        if (!metaObject.hasSetter(fieldName)) {
            return;
        }
        if (ObjectUtil.isNull(metaObject.getValue(fieldName))) {
            this.setIfHasSetter(fieldName, fieldVal, metaObject);
        }
    }

    /**
     * 字段存在 setter 时赋值
     *
     * @param fieldName  字段名
     * @param fieldVal   字段值
     * @param metaObject 元对象
     */
    private void setIfHasSetter(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (metaObject.hasSetter(fieldName) && ObjectUtil.isNotNull(fieldVal)) {
            metaObject.setValue(fieldName, fieldVal);
        }
    }

    /**
     * 解析当前租户ID
     *
     * @return 租户ID, 上下文无有效值时返回默认租户ID
     */
    private Long resolveTenantId() {
        Long tenantId = parseId(TenantContext.getTenantId());
        if (ObjectUtil.isNotNull(tenantId)) {
            return tenantId;
        }
        return parseId(DefaultValueEnum.TENANT_ID_DEFAULT.getValue());
    }

    /**
     * 解析当前操作人ID
     *
     * @return 操作人ID, 上下文无有效值时返回默认操作人ID
     */
    private Long resolveOperatorId() {
        Long operatorId = parseId(UserContext.getUserId());
        if (ObjectUtil.isNotNull(operatorId)) {
            return operatorId;
        }
        return parseId(DefaultValueEnum.OPERATOR_DEFAULT.getValue());
    }

    /**
     * 将字符串ID解析为 Long, 解析失败时返回 null 并打印告警日志
     *
     * @param id 字符串ID
     * @return 解析后的ID, 无法解析时返回 null
     */
    private Long parseId(String id) {
        if (StrUtil.isBlank(id)) {
            return null;
        }
        Long value = Convert.toLong(id, null);
        if (ObjectUtil.isNull(value)) {
            log.warn("非法的ID值 [{}], 已忽略", id);
        }
        return value;
    }
}
