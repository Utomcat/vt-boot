package com.ranyk.vt.boot.example.web.freamwork.service.account;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.DataStatusEnum;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountUserConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountUserConnection;
import com.ranyk.vt.boot.example.web.freamwork.mapper.account.AccountMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.account.AccountUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 账户用户关联关系数据库操作接口对象
     */
    private final AccountUserRepository accountUserRepository;
    /**
     * 账户信息数据转换接口对象
     */
    private final AccountMapper accountMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注册 AccountUserRepository 账户用户关联关系数据库操作接口对象 和 AccountMapper 账户信息数据转换接口对象
     *
     * @param accountUserRepository 账户用户关联关系数据库操作接口对象, {@link AccountUserRepository}
     * @param accountMapper         账户信息数据转换接口对象, {@link AccountMapper}
     */
    @Autowired
    public AccountUserConnectionService(AccountUserRepository accountUserRepository, AccountMapper accountMapper) {
        this.accountUserRepository = accountUserRepository;
        this.accountMapper = accountMapper;
    }

    /**
     * 保存账户用户关联关系信息
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @return 保存结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOneAccountUserConnectionInfo(AccountUserConnectionDTO accountUserConnectionDTO) {
        verifyAccountUserConnectionParam(accountUserConnectionDTO, OperateTypeEnum.SAVE);
        Integer countByAccountId = this.countByAccountId(accountUserConnectionDTO);
        if (countByAccountId > 0) {
            log.error("绑定账户用户关系失败, 账户 ID => {} 已存在账户用户关系信息!", accountUserConnectionDTO.getAccountId());
            throw new ServiceException("绑定账户用户关系失败, 账户 ID => %s 已存在账户用户关系信息!".formatted(accountUserConnectionDTO.getAccountId()));
        }
        Integer countByUserId = this.countByUserId(accountUserConnectionDTO);
        if (countByUserId > 0) {
            log.error("绑定账户用户关系失败, 用户 ID => {} 已存在账户用户关系信息!", accountUserConnectionDTO.getUserId());
            throw new ServiceException("绑定账户用户关系失败, 用户 ID => %s 已存在账户用户关系信息!".formatted(accountUserConnectionDTO.getUserId()));
        }
        AccountUserConnection accountUserConnection = accountMapper.accountUserConnectionDTOToAccountUserConnection(accountUserConnectionDTO);
        return this.saveOrUpdate(accountUserConnection);
    }

    /**
     * 删除账户用户关联关系信息
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteOneAccountUserConnectionInfoByAccountIdAndUserId(AccountUserConnectionDTO accountUserConnectionDTO) {
        verifyAccountUserConnectionParam(accountUserConnectionDTO, OperateTypeEnum.DELETE);
        return this.accountUserRepository.deleteOneByAccountIdEqAndUserIdEq(accountUserConnectionDTO.getAccountId(), accountUserConnectionDTO.getUserId());
    }

    /**
     * 根据账户用户关联关系ID删除账户用户关联关系信息
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteOneAccountUserConnectionInfoById(AccountUserConnectionDTO accountUserConnectionDTO) {
        verifyAccountUserConnectionParam(accountUserConnectionDTO, OperateTypeEnum.DELETE);
        return this.accountUserRepository.deleteOneById(accountUserConnectionDTO.getId());
    }

    /**
     * 根据账户ID查询账户用户关联关系信息
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @return 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     */
    public AccountUserConnectionDTO queryByAccountId(AccountUserConnectionDTO accountUserConnectionDTO) {
        if (StrUtil.isBlank(accountUserConnectionDTO.getAccountId())) {
            log.error("根据账户ID查询账户用户关联关系信息, 账户ID不能为空");
            throw new ServiceException("根据账户ID查询账户用户关联关系信息失败, 账户ID不能为空!");
        }
        LambdaQueryWrapper<AccountUserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountUserConnection::getAccountId, accountUserConnectionDTO.getAccountId());
        AccountUserConnection accountUserConnection = accountUserRepository.selectOne(queryWrapper);
        return accountMapper.accountUserConnectionToAccountUserConnectionDTO(accountUserConnection);
    }

    /**
     * 根据账户ID查询账户用户关联关系信息数量
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @return 账户用户关联关系信息数量
     */
    public Integer countByAccountId(AccountUserConnectionDTO accountUserConnectionDTO) {
        if (StrUtil.isBlank(accountUserConnectionDTO.getAccountId())) {
            log.error("根据账户 ID 查询是否存在账户用户关联关系信息失败, 需要查询的账户 ID => {} 不能为空!", accountUserConnectionDTO.getAccountId());
            throw new ServiceException("根据账户 ID 查询是否存在账户用户关联关系信息失败, 需要查询的账户 ID 不能为空!");
        }
        LambdaQueryWrapper<AccountUserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountUserConnection::getAccountId, accountUserConnectionDTO.getAccountId());
        long count = this.count(queryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * 根据用户ID查询账户用户关联关系信息数量
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @return 账户用户关联关系信息数量
     */
    public Integer countByUserId(AccountUserConnectionDTO accountUserConnectionDTO) {
        if (StrUtil.isBlank(accountUserConnectionDTO.getUserId())) {
            log.error("根据用户 ID 查询是否存在账户用户关联关系信息失败, 需要查询的用户 ID => {} 不能为空!", accountUserConnectionDTO.getUserId());
            throw new ServiceException("根据用户 ID 查询是否存在账户用户关联关系信息失败, 需要查询的用户 ID 不能为空!");
        }
        LambdaQueryWrapper<AccountUserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountUserConnection::getUserId, accountUserConnectionDTO.getUserId());
        long count = this.count(queryWrapper);
        return Integer.parseInt(String.valueOf(count));
    }

    /**
     * 验证账户用户关联关系参数
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @param operateType              操作类型, {@link OperateTypeEnum}
     */
    private void verifyAccountUserConnectionParam(AccountUserConnectionDTO accountUserConnectionDTO, OperateTypeEnum operateType) {
        switch (operateType) {
            case SAVE -> verifySaveAccountUserConnectionParam(accountUserConnectionDTO);
            case DELETE -> verifyDeleteAccountUserConnectionParam(accountUserConnectionDTO);
            default -> throw new ServiceException("操作账户用户关联关系信息失败, 操作类型不存在");
        }
    }

    /**
     * 验证账户用户关联关系参数 - 保存单个账户用户关联关系信息
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     */
    private void verifySaveAccountUserConnectionParam(AccountUserConnectionDTO accountUserConnectionDTO) {
        if (StrUtil.isBlank(accountUserConnectionDTO.getAccountId())) {
            log.error("保存账户用户关联关系信息失败, 账户ID不能为空!");
            throw new ServiceException("保存账户用户关联关系信息失败, 账户ID不能为空!");
        }
        if (StrUtil.isBlank(accountUserConnectionDTO.getUserId())) {
            log.error("保存账户用户关联关系信息失败, 用户ID不能为空!");
            throw new ServiceException("保存账户用户关联关系信息失败, 用户ID不能为空!");
        }
        accountUserConnectionDTO.setCreateBy(StpUtil.getLoginIdAsString());
        accountUserConnectionDTO.setUpdateBy(StpUtil.getLoginIdAsString());
        accountUserConnectionDTO.setStatus(DataStatusEnum.NORMAL.getValue());
    }

    /**
     * 验证账户用户关联关系参数 - 删除单个账户用户关联关系信息
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     */
    private void verifyDeleteAccountUserConnectionParam(AccountUserConnectionDTO accountUserConnectionDTO) {
        boolean idIsNotBlank = StrUtil.isNotBlank(accountUserConnectionDTO.getId());
        boolean accountIdIsBlank = StrUtil.isBlank(accountUserConnectionDTO.getAccountId());
        boolean userIdIsBlank = StrUtil.isBlank(accountUserConnectionDTO.getUserId());
        if (idIsNotBlank) {
            if (!accountIdIsBlank) {
                log.error("删除账户用户关联关系信息失败, 当前已提供了数据 ID, 不需要再提供账户 ID!");
                throw new ServiceException("删除账户用户关联关系信息失败, 当前已提供了数据 ID, 不需要再提供账户 ID!");
            }
            if (!userIdIsBlank) {
                log.error("删除账户用户关联关系信息失败, 当前已提供了数据 ID, 不需要再提供用户 ID!");
                throw new ServiceException("删除账户用户关联关系信息失败, 当前已提供了数据 ID, 不需要再提供用户 ID!");
            }
            return;
        }
        if (accountIdIsBlank) {
            log.error("删除账户用户关联关系信息失败, 账户ID不能为空!");
            throw new ServiceException("删除账户用户关联关系信息失败, 账户ID不能为空!");
        }
        if (userIdIsBlank) {
            log.error("删除账户用户关联关系信息失败, 用户ID不能为空!");
            throw new ServiceException("删除账户用户关联关系信息失败, 用户ID不能为空!");
        }

    }
}
