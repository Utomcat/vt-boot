package com.ranyk.vt.boot.example.web.freamwork.service.account;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountUserConnection;
import com.ranyk.vt.boot.example.web.freamwork.repository.account.AccountUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: AccountUserConnectionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户关联关系业务逻辑类
 * @date: 2026-03-17
 */
@Slf4j
@Service
public class AccountUserConnectionService extends ServiceImpl<AccountUserRepository, AccountUserConnection> {
}
