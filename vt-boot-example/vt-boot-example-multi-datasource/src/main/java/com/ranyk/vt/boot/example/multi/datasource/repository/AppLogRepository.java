package com.ranyk.vt.boot.example.multi.datasource.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.multi.datasource.domain.entity.AppLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: AppLogRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志数据库操作接口
 * @date: 2026-02-26
 */
@Mapper
@Component
public interface AppLogRepository extends BaseMapper<AppLog> {

    /**
     * 插入一条业务日志数据
     *
     * @param appLog 业务日志数据对象 {@link AppLog}
     * @return 插入结果, true: 插入成功; false: 插入失败;
     */
    Boolean insertOneData(@Param("appLog") AppLog appLog);
}
