package com.ranyk.vt.boot.example.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.menu.domain.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: SysMenuMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 菜单与权限表 Mapper
 * @date: 2026-11-20
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
}
