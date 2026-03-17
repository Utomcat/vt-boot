package com.ranyk.vt.boot.example.web.freamwork.api.account;

import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.po.DeleteAccountPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.po.QueryAccountPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.po.SaveAccountPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.po.UpdateAccountPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.vo.QueryAccountVO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.account.AccountMapper;
import com.ranyk.vt.boot.example.web.freamwork.service.account.AccountService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.MultiResult;
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
     * 新增一个账户信息
     *
     * @param saveAccountPO 账户信息数据封装对象 {@link SaveAccountPO}
     * @return 返回新增账户信息操作结果 {@link Boolean}
     */
    @PostMapping
    @Log(operation = "新增一个账户信息", type = Log.LogType.INSERT)
    public Result<Boolean> saveAccountInfo(@RequestBody SaveAccountPO saveAccountPO) {
        accountService.saveOneAccount(accountMapper.saveAccountPOToAccountDTO(saveAccountPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除一个账户信息
     *
     * @param deleteAccountPO 账户信息数据封装对象 {@link DeleteAccountPO}
     * @return 删除账户信息操作结果 {@link Boolean}
     */
    @DeleteMapping
    @Log(operation = "删除一个账户信息", type = Log.LogType.DELETE)
    public Result<Boolean> deleteAccountInfo(@RequestBody DeleteAccountPO deleteAccountPO) {
        accountService.deleteOneAccount(accountMapper.deleteAccountPOToAccountDTO(deleteAccountPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 批量删除账户信息
     *
     * @param deleteAccountPO 账户信息数据封装对象 {@link DeleteAccountPO}
     * @return 批量删除账户信息操作结果 {@link Boolean}
     */
    @DeleteMapping("/batch/delete/account")
    @Log(operation = "批量删除账户信息", type = Log.LogType.DELETE)
    public Result<Boolean> batchDeleteAccountInfo(@RequestBody DeleteAccountPO deleteAccountPO) {
        accountService.batchDeleteAccount(accountMapper.deleteAccountPOToAccountDTO(deleteAccountPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 更新一个账户信息
     *
     * @param updateAccountPO 账户信息数据封装对象 {@link UpdateAccountPO}
     * @return 更新账户信息操作结果 {@link Boolean}
     */
    @PutMapping
    @Log(operation = "更新一个账户信息", type = Log.LogType.UPDATE)
    public Result<Boolean> updateAccountInfo(@RequestBody UpdateAccountPO updateAccountPO) {
        accountService.updateOneAccount(accountMapper.updateAccountPOToAccountDTO(updateAccountPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询账户信息 - 分页
     *
     * @param queryAccountPO 账户信息数据封装对象 {@link QueryAccountPO}
     * @return 查询账户信息操作结果 {@link Boolean}
     */
    @GetMapping
    @Log(operation = "查询账户信息 - 分页", type = Log.LogType.SELECT)
    public MultiResult<QueryAccountVO> queryAccountInfo(QueryAccountPO queryAccountPO) {
        PageResponse<AccountDTO> accountDTOPageResponse = accountService.queryAccountByConditions(accountMapper.queryAccountPOToAccountDTO(queryAccountPO));
        return MultiResult.successMulti(accountMapper.accountDTOListToQueryAccountVOList(accountDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(accountDTOPageResponse.getTotal())),
                accountDTOPageResponse.getCurrentPage(),
                accountDTOPageResponse.getPageSize());
    }


}
