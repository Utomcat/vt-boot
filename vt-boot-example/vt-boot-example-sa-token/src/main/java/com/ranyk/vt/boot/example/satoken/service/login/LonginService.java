package com.ranyk.vt.boot.example.satoken.service.login;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.satoken.domain.login.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.domain.login.entity.Account;
import com.ranyk.vt.boot.example.satoken.mapper.login.AccountMapper;
import com.ranyk.vt.boot.example.satoken.repository.login.LoginAccountRepository;
import com.ranyk.vt.boot.example.satoken.service.captcha.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * CLASS_NAME: LonginService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录业务逻辑类
 * @date: 2026-03-01
 */
@Slf4j
@Service
public class LonginService extends ServiceImpl<LoginAccountRepository, Account> {

    /**
     * 登录账户数据库操作接口对象
     */
    private final LoginAccountRepository loginAccountRepository;
    /**
     * 账户信息数据对象转换映射对象
     */
    private final AccountMapper accountMapper;
    /**
     * 验证码业务逻辑类对象
     */
    private final CaptchaService captchaService;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 LoginAccountRepository 对象, AccountMapper 对象
     *
     * @param loginAccountRepository 登录账户数据库操作接口对象 {@link LoginAccountRepository}
     * @param accountMapper          账户信息数据对象转换映射对象 {@link AccountMapper}
     * @param captchaService         验证码业务逻辑类对象 {@link CaptchaService}
     */
    @Autowired
    public LonginService(LoginAccountRepository loginAccountRepository, AccountMapper accountMapper, CaptchaService captchaService) {
        this.loginAccountRepository = loginAccountRepository;
        this.accountMapper = accountMapper;
        this.captchaService = captchaService;
    }

    /**
     * 登录业务逻辑
     *
     * @param accountDTO 登录参数数据封装对象, {@link AccountDTO}
     * @return 登录结果, {@link AccountDTO}
     */
    public AccountDTO login(AccountDTO accountDTO) {
        // 校验验证码
        if (!captchaService.verifyCaptcha(accountDTO.getCaptcha())) {
            throw new ServiceException("user.captcha.error");
        }
        // 删除验证码
        captchaService.deleteCaptcha(accountDTO.getCaptcha());
        // 查询用户信息
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserName, accountDTO.getUserName());
        queryWrapper.eq(Account::getPassword, SaSecureUtil.md5(accountDTO.getPassword()));
        Account account = loginAccountRepository.selectOne(queryWrapper);
        if (Objects.isNull(account)) {
            throw new ServiceException("user.password.not.match");
        }
        // 执行用户登录操作
        StpUtil.login(account.getId());
        // 返回登录后的 token 信息
        return AccountDTO.builder().token(StpUtil.getTokenInfo().getTokenValue()).build();
    }
}
