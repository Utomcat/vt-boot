package com.ranyk.vt.boot.example.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.role.domain.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: SysUserRoleMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户-角色关联表 Mapper
 * @date: 2026-11-20
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
