package com.ranyk.vt.boot.example.satoken.api.account;

import com.ranyk.vt.boot.example.satoken.domain.account.po.AccountPO;
import com.ranyk.vt.boot.example.satoken.mapper.account.AccountMapper;
import com.ranyk.vt.boot.example.satoken.service.account.AccountService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CLASS_NAME: AccountApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户信息API
 * @date: 2026-03-03
 */
@RestController
@RequestMapping("/api/account")
public class AccountApi {

    /**
     * 账户信息业务逻辑类对象
     */
    private final AccountService accountService;
    /**
     * 账户信息数据对象转换映射接口对象
     */
    private final AccountMapper accountMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入账户信息业务逻辑类对象
     *
     * @param accountService 账户信息业务逻辑类对象 {@link AccountService}
     * @param accountMapper  账户信息数据对象转换映射接口对象 {@link AccountMapper}
     */
    @Autowired
    public AccountApi(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    /**
     * 新增账户信息
     *
     * @param accountPO 账户信息数据封装对象 {@link AccountPO}
     * @return 返回新增账户信息操作结果 {@link Boolean}
     */
    @PostMapping
    @Log(operation = "新增账户信息", type = Log.LogType.INSERT)
    public Result<Boolean> saveAccountInfo(@RequestBody AccountPO accountPO) {
        accountService.saveOne(accountMapper.saveRequestPOToDTO(accountPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除账户信息
     *
     * @param accountPO 账户信息数据封装对象 {@link AccountPO}
     * @return 删除账户信息操作结果 {@link Boolean}
     */
    @DeleteMapping
    @Log(operation = "删除账户信息", type = Log.LogType.DELETE)
    public Result<Boolean> deleteAccountInfo(@RequestBody AccountPO accountPO) {
        accountService.deleteOne(accountMapper.deleteRequestPOToDTO(accountPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 更新账户信息
     *
     * @param accountPO 账户信息数据封装对象 {@link AccountPO}
     * @return 更新账户信息操作结果 {@link Boolean}
     */
    @PutMapping
    @Log(operation = "更新账户信息", type = Log.LogType.UPDATE)
    public Result<Boolean> updateAccountInfo(@RequestBody AccountPO accountPO) {
        accountService.updateOne(accountMapper.updateRequestPOToDTO(accountPO));
        return Result.success(Boolean.TRUE);
    }
}
