package com.ranyk.vt.boot.example.satoken.repository.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.role.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: RoleRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库角色信息表操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface RoleRepository extends BaseMapper<Role> {
}
