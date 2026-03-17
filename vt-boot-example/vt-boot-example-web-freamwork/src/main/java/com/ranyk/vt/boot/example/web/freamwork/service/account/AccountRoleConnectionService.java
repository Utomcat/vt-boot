package com.ranyk.vt.boot.example.web.freamwork.service.account;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateType;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountRoleConnection;
import com.ranyk.vt.boot.example.web.freamwork.mapper.account.AccountMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.account.AccountRoleConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 保存账户角色关联关系 - 保存单条账户角色关联关系数据
     *
     * @param accountRoleConnectionDTO 账户角色关联关系保存参数封装对象 {@link AccountRoleConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneAccountRoleConnection(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        // 参数校验 - 保存账户角色关联关系参数校验
        verifyAccountRoleConnectionParams(accountRoleConnectionDTO, OperateType.SAVE);
        LambdaQueryWrapper<AccountRoleConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountRoleConnection::getAccountId, accountRoleConnectionDTO.getAccountId());
        queryWrapper.eq(AccountRoleConnection::getRoleId, accountRoleConnectionDTO.getRoleId());
        Long count = accountRoleConnectionRepository.selectCount(queryWrapper);
        if (count > 0){
            log.error("保存账户角色关联关系失败, 账户 ID: {}, 角色 ID: {}, 账户角色关联关系已存在!", accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId());
            throw new ServiceException("保存账户角色关联关系失败, 账户 ID: %s, 角色 ID: %s, 账户角色关联关系已存在!".formatted(accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId()));
        }
        AccountRoleConnection accountRoleConnection = accountMapper.accountRoleConnectionDTOToAccountRoleConnection(accountRoleConnectionDTO);
        accountRoleConnection.setCreateBy(StpUtil.getLoginIdAsString());
        accountRoleConnection.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean saveResult = this.saveOrUpdate(accountRoleConnection);
        if (!saveResult){
            log.error("保存 账户 ID: {}, 角色 ID: {} 的账户角色关联关系失败!", accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId());
            throw new ServiceException("保存 账户 ID: %s, 角色 ID: %s 的账户角色关联关系失败!".formatted(accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId()));
        }
    }

    /**
     * 删除账户角色关联关系 - 删除单条账户角色关联关系数据
     *
     * @param accountRoleConnectionDTO 账户角色关联关系删除参数封装对象 {@link AccountRoleConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneAccountRoleConnection(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        verifyAccountRoleConnectionParams(accountRoleConnectionDTO, OperateType.DELETE);
        LambdaQueryWrapper<AccountRoleConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountRoleConnection::getId, accountRoleConnectionDTO.getId());
        Long count = this.accountRoleConnectionRepository.selectCount(queryWrapper);
        if (count <= 0){
            log.error("删除账户角色关联关系处 账户 ID: {} - 角色 ID: {}  的账户角色关联关系不存在!", accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId());
            throw new ServiceException("删除账户角色关联关系处 账户 ID: %s - 角色 ID: %s  的账户角色关联关系不存在!".formatted(accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId()));
        }
        boolean deleteResult = this.accountRoleConnectionRepository.deleteByIdEq(accountRoleConnectionDTO.getId());
        if (!deleteResult){
            log.error("删除账户角色关联关系处 账户 ID: {} - 角色 ID: {} 的账户角色关联关系删除失败!", accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId());
            throw new ServiceException("删除账户角色关联关系处 账户 ID: %s - 角色 ID: %s 的账户角色关联关系删除失败!".formatted(accountRoleConnectionDTO.getAccountId(), accountRoleConnectionDTO.getRoleId()));
        }
    }

    /**
     * 删除账户角色关联关系 - 批量删除账户角色关联关系数据
     *
     * @param accountRoleConnectionDTO 账户角色关联关系删除参数封装对象集合 {@link AccountRoleConnectionDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAccountRoleConnection(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        verifyAccountRoleConnectionParams(accountRoleConnectionDTO, OperateType.BATCH_DELETE);
        Boolean deleteResult =this.accountRoleConnectionRepository.deleteByIdIn(accountRoleConnectionDTO.getIds());
        if (!deleteResult){
            log.error("批量删除账户角色关联关系处, 账户角色关联关系批量删除失败!");
            throw new ServiceException("批量删除账户角色关联关系处, 账户角色关联关系批量删除失败!");
        }
    }

    /**
     * 删除账户角色关联关系 - 通过 账户ID 删除账户角色关联关系数据
     *
     * @param accountRoleConnectionDTO 账户角色关联关系删除参数封装对象 {@link AccountRoleConnectionDTO}, 当前方法只使用了 {@link AccountRoleConnectionDTO#getAccountId()} 属性
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByAccountId(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        if (StrUtil.isBlank(accountRoleConnectionDTO.getAccountId())){
            log.error("删除账户角色关联关系失败, 需要删除的账户 ID 不能为空!");
            throw new ServiceException("删除账户角色关联关系失败, 需要删除的账户 ID 不能为空!");
        }
        Boolean deleteResult = this.accountRoleConnectionRepository.deleteByAccountId(accountRoleConnectionDTO.getAccountId());
        if (!deleteResult){
            log.error("删除账户角色关联关系失败, 账户 ID: {}", accountRoleConnectionDTO.getAccountId());
            throw new ServiceException("删除账户角色关联关系失败, 账户 ID: %s".formatted(accountRoleConnectionDTO.getAccountId()));
        }
        return Boolean.TRUE;
    }

    /**
     * 删除账户角色关联关系 - 通过 账户ID 批量删除账户角色关联关系数据
     *
     * @param accountRoleConnectionDTO 账户角色关联关系删除参数封装对象 {@link AccountRoleConnectionDTO}, 当前方法只使用了 {@link AccountRoleConnectionDTO#getAccountIds()} 集合属性
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByAccountIds(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        if (CollUtil.isEmpty(accountRoleConnectionDTO.getAccountIds())){
            log.error("批量删除账户角色关联关系失败, 需要删除的账户 ID 集合不能为空!");
            throw new ServiceException("批量删除账户角色关联关系失败, 需要删除的账户 ID 集合不能为空!");
        }
        Boolean deleteResult = this.accountRoleConnectionRepository.deleteByAccountIdIn(accountRoleConnectionDTO.getAccountIds());
        if (!deleteResult){
            log.error("批量删除账户角色关联关系失败, 账户 ID 集合: {}", accountRoleConnectionDTO.getAccountIds());
            throw new ServiceException("批量删除账户角色关联关系失败, 账户 ID 集合: %s".formatted(accountRoleConnectionDTO.getAccountIds()));
        }
        return Boolean.TRUE;
    }

    /**
     * 查询账户角色关联关系 - 通过 账户ID 查询账户角色关联关系数据
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
        return accountMapper.accountRoleConnectionListToAccountRoleConnectionDTOList(Optional.ofNullable(accountRoleConnectionList).orElse(Collections.emptyList()));
    }

    /**
     * 验证账户角色关联关系参数
     *
     * @param accountRoleConnectionDTO 账户角色关联关系参数封装对象 {@link AccountRoleConnectionDTO}
     * @param operateType              操作类型 {@link OperateType}
     */
    private void verifyAccountRoleConnectionParams(AccountRoleConnectionDTO accountRoleConnectionDTO, OperateType operateType) {
        switch (operateType){
            case SAVE -> verifySaveAccountRoleConnectionParams(accountRoleConnectionDTO);
            case DELETE -> verifyDeleteAccountRoleConnectionParams(accountRoleConnectionDTO);
            case BATCH_DELETE -> verifyBatchDeleteAccountRoleConnectionParams(accountRoleConnectionDTO);
            case UPDATE -> verifyUpdateAccountRoleConnectionParams(accountRoleConnectionDTO);
            default -> throw new ServiceException("验证账户角色关联关系参数, 验证的操作类型错误!");
        }
    }

    /**
     * 验证账户角色关联关系参数 - 保存账户角色关联关系参数校验
     */
    private void verifySaveAccountRoleConnectionParams(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        // 参数校验 - 账户ID 不能为空
        if (StrUtil.isBlank(accountRoleConnectionDTO.getAccountId())) {
            log.error("保存账户角色关联关系参数校验失败, 需要保存的 账户 ID 不能为空!");
            throw new ServiceException("保存账户角色关联关系参数校验失败, 需要保存的 账户 ID 不能为空!");
        }
        // 参数校验 - 角色ID 不能为空
        if (StrUtil.isBlank(accountRoleConnectionDTO.getRoleId())) {
            log.error("保存账户角色关联关系参数校验失败, 需要保存的 角色 ID 不能为空!");
            throw new ServiceException("保存账户角色关联关系参数校验失败, 需要保存的 角色 ID 不能为空!");
        }
    }

    /**
     * 验证账户角色关联关系参数 - 删除单条账户角色关联关系参数校验
     */
    private void verifyDeleteAccountRoleConnectionParams(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        if (StrUtil.isBlank(accountRoleConnectionDTO.getId()) && CollUtil.isEmpty(accountRoleConnectionDTO.getIds())) {
            log.error("删除账户角色关联关系参数校验失败, 需要删除的账户角色关联关系 ID 不能为空!");
            throw new ServiceException("删除账户角色关联关系参数校验失败, 需要删除的账户角色关联关系 ID 不能为空!");
        }
    }

    /**
     * 验证账户角色关联关系参数 - 批量删除账户角色关联关系参数校验
     */
    private void verifyBatchDeleteAccountRoleConnectionParams(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        // 参数校验 - 账户角色关联关系 ID 集合不能为空
        if (CollUtil.isEmpty(accountRoleConnectionDTO.getIds())) {
            log.error("批量删除账户角色关联关系参数校验失败, 需要删除的账户角色关联关系 ID 集合不能为空!");
            throw new ServiceException("批量删除账户角色关联关系参数校验失败, 需要删除的账户角色关联关系 ID 集合不能为空!");
        }
    }

    /**
     * 验证账户角色关联关系参数 - 更新账户角色关联关系参数校验
     */
    private void verifyUpdateAccountRoleConnectionParams(AccountRoleConnectionDTO accountRoleConnectionDTO) {
        // 参数校验 - 账户角色关联关系 ID 不能为空
        if (StrUtil.isBlank(accountRoleConnectionDTO.getId())) {
            log.error("更新账户角色关联关系参数校验失败, 需要更新的账户角色关联关系 ID 不能为空!");
            throw new ServiceException("更新账户角色关联关系参数校验失败, 需要更新的账户角色关联关系 ID 不能为空!");
        }
    }
}
