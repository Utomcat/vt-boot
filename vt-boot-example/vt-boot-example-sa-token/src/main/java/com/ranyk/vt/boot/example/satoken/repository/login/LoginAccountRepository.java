package com.ranyk.vt.boot.example.satoken.repository.login;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.login.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: LoginAccountRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录账户数据库操作接口
 * @date: 2026-03-01
 */
@Mapper
@Component
public interface LoginAccountRepository extends BaseMapper<Account> {
}
