package com.ranyk.vt.boot.example.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.user.domain.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: SysUserMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户表 Mapper
 * @date: 2026-11-16
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
