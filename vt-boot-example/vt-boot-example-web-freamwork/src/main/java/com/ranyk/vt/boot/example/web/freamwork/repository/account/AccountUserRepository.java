package com.ranyk.vt.boot.example.web.freamwork.repository.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountUserConnection;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: AccountUserRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户数据库操作接口
 * @date: 2026-03-17
 */
@Mapper
@Component
public interface AccountUserRepository extends BaseMapper<AccountUserConnection> {
}
