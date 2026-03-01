package com.ranyk.vt.boot.example.satoken.api.login;

import com.ranyk.vt.boot.example.satoken.domain.login.po.AccountPO;
import com.ranyk.vt.boot.example.satoken.domain.login.vo.AccountVO;
import com.ranyk.vt.boot.example.satoken.mapper.login.AccountMapper;
import com.ranyk.vt.boot.example.satoken.service.login.LonginService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: LoginApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录接口API
 * @date: 2026-03-01
 */
@RestController
@RequestMapping("/api/login")
public class LoginApi {

    /**
     * 登录业务逻辑对象
     */
    private final LonginService longinService;
    /**
     * 账户信息数据对象转换映射对象
     */
    private final AccountMapper accountMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 LonginService 对象, AccountMapper 对象
     *
     * @param longinService 登录业务逻辑对象 {@link LonginService}
     * @param accountMapper 账户信息数据对象转换映射对象 {@link AccountMapper}
     */
    @Autowired
    public LoginApi(LonginService longinService, AccountMapper accountMapper) {
        this.longinService = longinService;
        this.accountMapper = accountMapper;
    }

    /**
     * 登录接口
     *
     * @param accountPO 登录参数数据封装对象, {@link AccountPO}
     * @return 登录结果, {@link AccountVO}
     */
    @PostMapping
    @Log(operation = "登录接口", type = Log.LogType.ACCESS)
    public Result<AccountVO> login(@RequestBody AccountPO accountPO) {
        return Result.<AccountVO>builder().data(accountMapper.dtoToVO(longinService.login(accountMapper.loginRequestPOToDTO(accountPO)))).build();
    }

}
