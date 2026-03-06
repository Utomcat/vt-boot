package com.ranyk.vt.boot.example.satoken.service.account;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.DataStatusEnum;
import com.ranyk.vt.boot.base.constant.OperateType;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.Account;
import com.ranyk.vt.boot.example.satoken.mapper.account.AccountMapper;
import com.ranyk.vt.boot.example.satoken.repository.account.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * CLASS_NAME: AccountService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户业务逻辑类
 * @date: 2026-03-03
 */
@Slf4j
@Service
public class AccountService extends ServiceImpl<AccountRepository, Account> {
    /**
     * 登录账户数据库操作接口对象
     */
    private final AccountRepository accountRepository;
    /**
     * 账户信息数据对象转换映射接口对象
     */
    private final AccountMapper accountMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 AccountRepository 对象
     *
     * @param accountRepository 登录账户数据库操作接口对象 {@link AccountRepository}
     * @param accountMapper     账户信息数据对象转换映射接口对象 {@link AccountMapper}
     */
    @Autowired
    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * 新增账户信息
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOne(AccountDTO accountDTO) {
        // 验证用户名和密码是否存在值
        verifyAccountParams(accountDTO, OperateType.SAVE);
        // 通过账户名查询账户信息
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        // 查询条件 - 账户名
        queryWrapper.eq(Account::getUserName, accountDTO.getUserName());
        // 执行查询结果 - 是否存在相同账户名的数据
        Long count = accountRepository.selectCount(queryWrapper);
        // 存在相同账户名的数据
        if (count > 0) {
            log.error("新增一条账户信息操作 - 账户名已存在!");
            throw new ServiceException("user.username.exists");
        }
        // 数据转换 - 账户信息数据传输对象 转换为 数据实体对象
        Account account = accountMapper.dtoToEntity(accountDTO);
        // 密码进行加密处理
        account.setPassword(SaSecureUtil.md5(account.getPassword()));
        account.setCreateBy(StpUtil.getLoginIdAsString());
        account.setUpdateBy(StpUtil.getLoginIdAsString());
        // 保存账户信息
        boolean saveResult = saveOrUpdate(account);
        // 判断保存结果是否成功
        if (!saveResult) {
            log.error("新增账户信息失败!");
            throw new ServiceException("新增账户信息失败!");
        }
    }

    /**
     * 删除账户信息
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOne(AccountDTO accountDTO) {
        // 验证账户ID是否存在值
        verifyAccountParams(accountDTO, OperateType.DELETE);
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getId, accountDTO.getId());
        Account account = accountRepository.selectOne(queryWrapper);
        LambdaUpdateWrapper<Account> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Account::getUpdateBy, StpUtil.getLoginIdAsString()).set(Account::getUpdateTime, LocalDateTime.now()).set(Account::getStatus, DataStatusEnum.DISABLE.getValue()).eq(Account::getId, account.getId());;
        boolean deleteResult = update(updateWrapper);
        if (!deleteResult) {
            log.error("删除账户信息失败!");
            throw new ServiceException("删除账户信息失败!");
        }
    }

    /**
     * 修改账户信息
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOne(AccountDTO accountDTO) {
        // 验证账户ID是否存在值
        verifyAccountParams(accountDTO, OperateType.UPDATE);
        Account account = accountRepository.selectOneAccountById(accountDTO.getId());
        if (Objects.isNull(account)) {
            throw new ServiceException("登录模块", "user.not.exists", new String[]{accountDTO.getUserName()});
        }
        // 当用户名存在时再进行用户名设置
        Optional.ofNullable(accountDTO.getUserName()).filter(StrUtil::isNotBlank).ifPresent(account::setUserName);
        // 当密码存在时再进行密码设置
        Optional.ofNullable(accountDTO.getPassword()).filter(StrUtil::isNotBlank).ifPresent(password -> account.setPassword(SaSecureUtil.md5(password)));
        // 当状态存在时再进行状态设置
        Optional.ofNullable(accountDTO.getStatus()).ifPresent(account::setStatus);
        // 当备注存在时再进行备注设置
        Optional.ofNullable(accountDTO.getRemark()).filter(StrUtil::isNotBlank).ifPresent(account::setRemark);
        account.setUpdateBy(StpUtil.getLoginIdAsString());
        account.setUpdateTime(LocalDateTime.now());
        // 执行修改操作
        boolean updateResult = accountRepository.updateOneAccountById(account);
        if (!updateResult) {
            log.error("修改账户信息失败!");
            throw new ServiceException("修改账户信息失败!");
        }
    }

    /**
     * 依据条件查询满足条件的一个账户信息 - 精确匹配
     *
     * @param accountDTO 账户信息查询参数封装对象 {@link AccountDTO}
     * @return 分页对象 {@link PageResponse} - 账户信息数据传输对象 {@link AccountDTO}
     */
    public PageResponse<AccountDTO> queryAccountByConditions(AccountDTO accountDTO) {
        IPage<Account> page = PageUtils.buildPage(accountDTO);
        // 创建查询条件对象
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(accountDTO.getUserName()), Account::getUserName, accountDTO.getUserName());
        queryWrapper.like(StrUtil.isNotBlank(accountDTO.getRemark()), Account::getRemark, accountDTO.getRemark());
        queryWrapper.eq(Objects.nonNull(accountDTO.getStatus()), Account::getStatus, accountDTO.getStatus());
        IPage<Account> accountPage = page(page, queryWrapper);
        return PageUtils.buildPageResponse(accountPage, accountMapper.accountEntityListToDTO(accountPage.getRecords()));
    }

    /**
     * 依据条件查询满足条件的账户信息
     *
     * @param accountDTO 账户信息查询参数封装对象 {@link AccountDTO}
     * @return 返回依据查询条件查询出的账户信息传输对象 {@link AccountDTO}
     */
    public AccountDTO queryOneAccountInfo(AccountDTO accountDTO) {
        // 验证用户名和密码是否存在值
        verifyAccountParams(accountDTO, OperateType.QUERY);
        // 查询用户信息
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(accountDTO.getUserName()), Account::getUserName, accountDTO.getUserName());
        queryWrapper.eq(StrUtil.isNotBlank(accountDTO.getPassword()), Account::getPassword, SaSecureUtil.md5(accountDTO.getPassword()));
        // 执行查询结果
        Account account = accountRepository.selectOne(queryWrapper);
        // 返回对应的账户信息数据传输对象
        return Optional.ofNullable(accountMapper.entityToDTO(account)).orElse(AccountDTO.builder().build());
    }

    /**
     * 验证账户信息参数
     *
     * @param accountDTO  账户信息数据传输对象 {@link AccountDTO}
     * @param operateType 操作类型 {@link OperateType}
     */
    private void verifyAccountParams(AccountDTO accountDTO, OperateType operateType) {
        switch (operateType) {
            case SAVE -> verifySaveAccountParams(accountDTO);
            case UPDATE -> verifyUpdateAccountParams(accountDTO);
            case DELETE -> verifyDeleteAccountParams(accountDTO);
            case QUERY -> verifyQueryAccountParams(accountDTO);
            default -> throw new ServiceException("操作类型不存在!");
        }

    }

    /**
     * 验证新增账户信息参数
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    private void verifySaveAccountParams(AccountDTO accountDTO) {
        if (StrUtil.isBlank(accountDTO.getUserName())) {
            log.error("新增账户信息 - 用户名不能为空!");
            throw new ServiceException("新增账户信息 - 用户名不能为空!");
        }
        if (StrUtil.isBlank(accountDTO.getPassword())) {
            log.error("新增账户信息 - 密码不能为空!");
            throw new ServiceException("新增账户信息 - 密码不能为空!");
        }
    }

    /**
     * 验证删除账户信息参数
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    private void verifyDeleteAccountParams(AccountDTO accountDTO) {
        if (StrUtil.isBlank(accountDTO.getId()) && CollectionUtil.isEmpty(accountDTO.getIds())) {
            log.error("删除账户信息 - 账户ID或账户ID列表不能为空!");
            throw new ServiceException("删除账户信息 - 账户ID或账户ID列表不能为空!");
        }
    }

    /**
     * 验证修改账户信息参数
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    private void verifyUpdateAccountParams(AccountDTO accountDTO) {
        if (StrUtil.isBlank(accountDTO.getId())) {
            log.error("修改账户信息 - 账户ID不能为空!");
            throw new ServiceException("修改账户信息 - 账户ID不能为空!");
        }
    }

    /**
     * 验证查询账户信息参数
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    private void verifyQueryAccountParams(AccountDTO accountDTO) {
        if (StrUtil.isBlank(accountDTO.getUserName())) {
            log.error("查询指定账户信息操作 - 用户名不能为空!");
            throw new ServiceException("user.username.not.blank");
        }
        if (StrUtil.isBlank(accountDTO.getPassword())) {
            log.error("查询指定账户信息操作 - 密码不能为空!");
            throw new ServiceException("user.password.not.blank");
        }
    }
}
