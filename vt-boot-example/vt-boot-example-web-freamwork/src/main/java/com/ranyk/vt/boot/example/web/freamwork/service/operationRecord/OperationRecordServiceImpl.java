package com.ranyk.vt.boot.example.web.freamwork.service.operationRecord;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.web.freamwork.domain.operationRecord.entity.OperationLog;
import com.ranyk.vt.boot.example.web.freamwork.repository.operationRecord.OperationRecordRepository;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * CLASS_NAME: OperationRecordServiceImpl.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作日志记录业务逻辑类
 * @date: 2026-03-26
 */
@Slf4j
@Service
public class OperationRecordServiceImpl extends ServiceImpl<OperationRecordRepository, OperationLog> implements IOperationRecordService {
    /**
     * 操作日志记录数据访问接口对象
     */
    private final OperationRecordRepository operationRecordRepository;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 OperationRecordRepository 对象
     *
     * @param operationRecordRepository 操作日志记录数据访问接口对象, {@link OperationRecordRepository}
     */
    @Autowired
    public OperationRecordServiceImpl(OperationRecordRepository operationRecordRepository) {
        this.operationRecordRepository = operationRecordRepository;
    }

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
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOneOperationRecord(String name, String type, String param, String operator, String module) {
        if (StrUtil.isBlank(module)) {
            log.error("未传入需要保存的操作记录日志表,不予进行数据保存操作!");
            return Boolean.FALSE;
        }
        if (Objects.equals(module, "operation_log_info")) {
            return this.saveOrUpdate(OperationLog.builder()
                    .name(name)
                    .type(type)
                    .param(param)
                    .operator(operator)
                    .createBy(operator)
                    .updateBy(operator)
                    .build());
        } else {
            return operationRecordRepository.insertOneOperationRecord(OperationLog.builder()
                    .id(UUID.fastUUID().toString().toLowerCase())
                    .name(name)
                    .type(type)
                    .param(param)
                    .operator(operator)
                    .createBy(operator)
                    .updateBy(operator)
                    .build(), module);
        }
    }
}
