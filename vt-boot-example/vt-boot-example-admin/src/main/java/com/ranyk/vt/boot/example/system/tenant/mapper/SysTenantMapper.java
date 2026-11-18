package com.ranyk.vt.boot.example.system.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.tenant.domain.entity.SysTenant;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: SysTenantMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户表 Mapper
 * @date: 2026-11-16
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {
}
