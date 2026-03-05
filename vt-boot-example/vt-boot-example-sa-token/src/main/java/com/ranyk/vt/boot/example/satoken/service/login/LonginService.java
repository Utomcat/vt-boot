package com.ranyk.vt.boot.example.satoken.service.login;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.ranyk.vt.boot.base.constant.TenantEnum;
import com.ranyk.vt.boot.base.context.TenantContext;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.service.account.AccountService;
import com.ranyk.vt.boot.example.satoken.service.captcha.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class LonginService {

    /**
     * 账户信息业务逻辑类对象
     */
    private final AccountService accountService;
    /**
     * 验证码业务逻辑类对象
     */
    private final CaptchaService captchaService;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 LoginAccountRepository 对象, AccountMapper 对象
     *
     * @param accountService 账户信息业务逻辑类对象 {@link AccountService}
     * @param captchaService 验证码业务逻辑类对象 {@link CaptchaService}
     */
    @Autowired
    public LonginService(AccountService accountService, CaptchaService captchaService) {
        this.accountService = accountService;
        this.captchaService = captchaService;
    }

    /**
     * 登录业务逻辑
     *
     * @param accountDTO 登录参数数据封装对象, {@link AccountDTO}
     * @return 登录结果, {@link AccountDTO}
     */
    public AccountDTO login(AccountDTO accountDTO) {
        // 验证用户名和密码是否为空
        if (StrUtil.isBlank(accountDTO.getUserName()) || StrUtil.isBlank(accountDTO.getPassword())) {
            throw new ServiceException("用户名或密码不能为空!");
        }
        // 校验验证码
        if (!captchaService.verifyCaptcha(accountDTO.getCaptchaKey(), accountDTO.getCaptcha())) {
            throw new ServiceException("user.captcha.error");
        }
        // 查询用户信息
        AccountDTO account = accountService.queryOneAccountInfo(accountDTO);
        // 判断用户信息是否为空
        if (StrUtil.isBlank(account.getId())) {
            throw new ServiceException("user.password.not.match");
        }
        // 执行用户登录操作
        StpUtil.login(account.getId());
        // 登录成功后,将当前用户的租户信息保存到 session 中
        StpUtil.getSession().set(TenantEnum.TENANT_ID.getValue(), account.getTenantId());
        // 登录成功后, 将当前用户的租户信息保存到 TenantContextHandler 中
        TenantContext.setTenantId(account.getTenantId());
        // 返回登录后的 token 信息
        return AccountDTO.builder().token(StpUtil.getTokenInfo().getTokenValue()).build();
    }
}
