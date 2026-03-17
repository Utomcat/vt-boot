package com.ranyk.vt.boot.example.web.freamwork.repository.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.role.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 依据条件 = name and != id 统计角色数据的数量
     *
     * @param name 角色名称
     * @param id 角色ID
     * @return 角色数据的数量
     */
    Integer selectCountByNameEqAndIdNotEq(@Param("name") String name, @Param("id") String id);

    /**
     * 依据条件 = code and != id 统计角色数据的数量
     *
     * @param code 角色编码
     * @param id 角色ID
     * @return 角色数据的数量
     */
    Integer selectCountByCodeEqAndIdNotEq(@Param("code") String code, @Param("id") String id);
}
