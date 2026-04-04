package com.ranyk.vt.boot.example.web.freamwork.repository.operationRecord;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.operationRecord.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: OperationRecordRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 操作日志记录 数据库操作接口
 * @date: 2026-03-26
 */
@Mapper
@Component
public interface OperationRecordRepository extends BaseMapper<OperationLog> {
    /**
     * 保存一条操作日志记录数据
     *
     * @param operationLog 操作日志记录数据对象 {@link OperationLog}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    Boolean insertOneOperationRecord(@Param("operationLog") OperationLog operationLog, @Param("tableName") String tableName);
}
