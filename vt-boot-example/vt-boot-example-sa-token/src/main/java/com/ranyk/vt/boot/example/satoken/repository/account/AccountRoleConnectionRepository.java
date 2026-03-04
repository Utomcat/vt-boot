package com.ranyk.vt.boot.example.satoken.repository.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.AccountRoleConnection;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: AccountRoleConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库账户角色关联关系操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface AccountRoleConnectionRepository extends BaseMapper<AccountRoleConnection> {
}
