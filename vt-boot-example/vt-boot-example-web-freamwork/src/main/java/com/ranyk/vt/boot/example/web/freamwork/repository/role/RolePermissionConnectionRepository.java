package com.ranyk.vt.boot.example.web.freamwork.repository.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.entity.RolePermissionConnection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CLASS_NAME: RolePermissionConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库角色权限数据表操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface RolePermissionConnectionRepository extends BaseMapper<RolePermissionConnection> {

    /**
     * 根据角色 ID List 删除所有角色关联的权限数据
     *
     * @param roleIds 角色 ID List
     */
    void deleteByRoleIdIn(@Param("roleIds") List<String> roleIds);
}
