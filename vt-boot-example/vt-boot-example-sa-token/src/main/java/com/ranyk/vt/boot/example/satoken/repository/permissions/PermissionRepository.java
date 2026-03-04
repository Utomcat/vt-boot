package com.ranyk.vt.boot.example.satoken.repository.permissions;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.permissions.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: PermissionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库权限信息表操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface PermissionRepository extends BaseMapper<Permission> {

}
