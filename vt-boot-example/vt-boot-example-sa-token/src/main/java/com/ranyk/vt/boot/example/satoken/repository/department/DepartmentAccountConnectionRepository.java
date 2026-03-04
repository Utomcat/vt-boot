package com.ranyk.vt.boot.example.satoken.repository.department;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.department.entity.DepartmentAccountConnection;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: DepartmentAccountConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库部门账户关联关系表操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface DepartmentAccountConnectionRepository extends BaseMapper<DepartmentAccountConnection> {
}
