package com.ranyk.vt.boot.log.service;

import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: DefaultOperationRecordService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 默认操作日志记录服务实现(空实现)
 * @date: 2026-04-08
 */
@Slf4j
@Service
@ConditionalOnMissingBean(IOperationRecordService.class)
public class DefaultOperationRecordService implements IOperationRecordService {

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
    @Override
    public Boolean saveOneOperationRecord(String name, String type, String param, String operator, String module) {
        log.debug("DefaultOperationRecordService: 操作日志记录功能未配置具体实现, 跳过保存. name={}, type={}, module={}", name, type, module);
        return Boolean.TRUE;
    }
}
