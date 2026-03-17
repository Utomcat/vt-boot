package com.ranyk.vt.boot.base.constant;

/**
 * CLASS_NAME: OperateType.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作类型枚举
 * @date: 2026-03-05
 */
public enum OperateType {
    /**
     * 单条数据新增/保存
     */
    SAVE,
    /**
     * 批量保存
     */
    BATCH_SAVE,
    /**
     * 单条数据删除
     */
    DELETE,
    /**
     * 批量删除
     */
    BATCH_DELETE,
    /**
     * 单条数据修改
     */
    UPDATE,
    /**
     * 批量修改
     */
    BATCH_UPDATE,
    /**
     * 查询
     */
    QUERY,
}
