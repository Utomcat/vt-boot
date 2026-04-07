package com.ranyk.vt.boot.example.web.freamwork.repository.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.tenant.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: TenantRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 租户数据库操作接口
 * @date: 2026-04-06
 */
@Mapper
@Component
public interface TenantRepository extends BaseMapper<Tenant> {
    /**
     * 依据条件 = name and != id 统计租户数据的数量
     *
     * @param name 租户名称
     * @param id 租户ID
     * @return 租户数据的数量
     */
    Integer selectCountByNameEqAndIdNotEq(@Param("name") String name, @Param("id") String id);

    /**
     * 依据条件 = code and != id 统计租户数据的数量
     *
     * @param code 租户编码
     * @param id 租户ID
     * @return 租户数据的数量
     */
    Integer selectCountByCodeEqAndIdNotEq(@Param("code") String code, @Param("id") String id);
}
