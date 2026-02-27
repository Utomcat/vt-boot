package com.ranyk.vt.boot.example.multi.datasource.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.multi.datasource.domain.entity.AppServiceTable;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: AppServiceTableRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务测试表数据库操作接口
 * @date: 2026-02-26
 */
@Mapper
@Component
public interface AppServiceTableRepository extends BaseMapper<AppServiceTable> {
}
