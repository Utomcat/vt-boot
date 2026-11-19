package com.ranyk.vt.boot.example.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: SysRoleMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 角色表 Mapper
 * @date: 2026-11-20
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
}
