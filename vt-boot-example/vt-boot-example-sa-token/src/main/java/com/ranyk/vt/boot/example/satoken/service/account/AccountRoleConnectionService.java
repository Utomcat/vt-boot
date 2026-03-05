package com.ranyk.vt.boot.example.satoken.service.account;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.AccountRoleConnection;
import com.ranyk.vt.boot.example.satoken.mapper.account.AccountMapper;
import com.ranyk.vt.boot.example.satoken.repository.account.AccountRoleConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * CLASS_NAME: AccountRoleConnectionService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户角色关联关系业务逻辑类
 * @date: 2026-03-03
 */
@Slf4j
@Service
public class AccountRoleConnectionService extends ServiceImpl<AccountRoleConnectionRepository, AccountRoleConnection> {

    /**
     * 账户角色关联关系数据操作接口对象
     */
    private final AccountRoleConnectionRepository accountRoleConnectionRepository;
    /**
     * 账户数据操作接口对象
     */
    private final AccountMapper accountMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入账户角色关联关系数据操作接口对象
     *
     * @param accountRoleConnectionRepository 账户角色关联关系数据操作接口对象
     * @param accountMapper                   账户数据操作接口对象
     */
    @Autowired
    public AccountRoleConnectionService(AccountRoleConnectionRepository accountRoleConnectionRepository, AccountMapper accountMapper) {
        this.accountRoleConnectionRepository = accountRoleConnectionRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * 通过 账户ID 查询账户角色关联关系数据
     *
     * @param accountRoleConnectionDTO 账户角色关联关系查询参数封装对象 {@link AccountRoleConnectionDTO}, 当前方法只使用了 {@link AccountRoleConnectionDTO#getAccountId()} 属性
     * @return 返回查询到的账户角色关联关系数据列表 {@link AccountRoleConnectionDTO}
     */
    public List<AccountRoleConnectionDTO> queryAccountRoleConnectionByAccountId(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        // 参数校验 - 账户ID 不能为空
        if (StrUtil.isBlank(accountRoleConnectionDTO.getAccountId())) {
            throw new ServiceException("查询账户角色关联关系失败, 需要查询的账户 ID 不能为空!");
        }
        // 构建查询条件对象
        LambdaQueryWrapper<AccountRoleConnection> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件 - 账户ID
        queryWrapper.eq(AccountRoleConnection::getAccountId, accountRoleConnectionDTO.getAccountId());
        // 执行查询
        List<AccountRoleConnection> accountRoleConnectionList = accountRoleConnectionRepository.selectList(queryWrapper);
        // 返回查询结果 - 转换为 DTO 列表
        return accountMapper.accountRoleConnectionEntityListToDTO(Optional.ofNullable(accountRoleConnectionList).orElse(Collections.emptyList()));
    }
}
