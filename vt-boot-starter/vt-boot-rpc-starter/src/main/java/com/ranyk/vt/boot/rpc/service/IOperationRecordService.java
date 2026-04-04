package com.ranyk.vt.boot.rpc.service;

/**
 * CLASS_NAME: IOperationRecordService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作日志记录业务接口
 * @date: 2026-03-26
 */
public interface IOperationRecordService {

    /**
     * 保存操作记录
     *
     * @param name     操作名称
     * @param type     操作类型
     * @param param    操作参数
     * @param operator 操作人
     * @param module   保存的数据表
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    Boolean saveOneOperationRecord(String name, String type, String param, String operator, String module);
}
