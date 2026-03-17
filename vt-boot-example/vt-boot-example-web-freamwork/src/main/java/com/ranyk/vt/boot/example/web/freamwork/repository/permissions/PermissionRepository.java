package com.ranyk.vt.boot.example.web.freamwork.repository.permissions;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.permissions.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据条件 = name and != id 统计权限数量
     *
     * @param name 权限名称
     * @param id 权限ID
     * @return 符合条件的权限数量
     */
    Integer selectCountByNameEqAndIdNotEq(@Param("name") String name, @Param("id") String id);

    /**
     * 根据条件 = code and != id 统计权限数量
     *
     * @param code 权限编码
     * @param id 权限ID
     * @return 符合条件的权限数量
     */
    Integer selectCountByCodeEqAndIdNotEq(@Param("code") String code, @Param("id") String id);

    /**
     * 根据ID 更新权限信息
     *
     * @param permission 待更新的权限信息数据封装对象, {@link Permission}
     * @return 更新结果
     */
    Boolean updateByIdEq(@Param("permission") Permission permission);
}
