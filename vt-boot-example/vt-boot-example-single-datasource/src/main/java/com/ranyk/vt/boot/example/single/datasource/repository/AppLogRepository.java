package com.ranyk.vt.boot.example.single.datasource.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.single.datasource.domain.entity.AppLog;
import org.apache.ibatis.annotations.Mapper;
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
}
