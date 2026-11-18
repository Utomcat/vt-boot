package com.ranyk.vt.boot.example.system.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.log.domain.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: LoginLogMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录审计流水 Mapper
 * @date: 2026-11-16
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
}
