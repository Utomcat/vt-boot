package com.ranyk.vt.boot.example.web.freamwork.service.account;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.Account;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentAccountConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentDTO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.account.AccountMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.account.AccountRepository;
import com.ranyk.vt.boot.example.web.freamwork.service.department.DepartmentAccountConnectionService;
import com.ranyk.vt.boot.example.web.freamwork.service.department.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * 部门信息业务逻辑类对象
     */
    private final DepartmentService departmentService;
    /**
     * 部门账户关联业务逻辑类对象
     */
    private final DepartmentAccountConnectionService departmentAccountConnectionService;
    /**
     * 账户角色关联业务逻辑类对象
     */
    private final AccountRoleConnectionService accountRoleConnectionService;
    /**
     * 账户信息数据对象转换映射接口对象
     */
    private final AccountMapper accountMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 AccountRepository 对象
     *
     * @param accountRepository                  登录账户数据库操作接口对象 {@link AccountRepository}
     * @param departmentService                  部门信息业务逻辑类对象 {@link DepartmentService}
     * @param departmentAccountConnectionService 部门账户关联业务逻辑类对象 {@link DepartmentAccountConnectionService}
     * @param accountRoleConnectionService       账户角色关联业务逻辑类对象 {@link AccountRoleConnectionService}
     * @param accountMapper                      账户信息数据对象转换映射接口对象 {@link AccountMapper}
     */
    @Autowired
    public AccountService(AccountRepository accountRepository,
                          DepartmentService departmentService,
                          DepartmentAccountConnectionService departmentAccountConnectionService,
                          AccountRoleConnectionService accountRoleConnectionService,
                          AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.departmentService = departmentService;
        this.departmentAccountConnectionService = departmentAccountConnectionService;
        this.accountRoleConnectionService = accountRoleConnectionService;
        this.accountMapper = accountMapper;
    }

    /**
     * 新增账户信息 - 保存单条账户信息数据
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneAccount(AccountDTO accountDTO) {
        // 验证用户名和密码是否存在值
        verifyAccountParams(accountDTO, OperateTypeEnum.SAVE);
        // 执行查询结果 - 通过账户名查询是否存在相同账户名的数据
        Integer count = accountRepository.selectCountByUserNameEq(accountDTO.getUserName());
        // 存在相同账户名的数据
        if (count > 0) {
            log.error("新增一条账户信息操作 - 账户名已存在!");
            throw new ServiceException("user.username.exists");
        }
        // 验证部门数据是否存在
        Integer departmentCount = departmentService.queryDepartmentCountById(DepartmentDTO.builder().id(accountDTO.getDepartmentId()).build());
        if (departmentCount <= 0) {
            log.error("新增账户信息操作 - 绑定部门账户关联信息时, 需绑定的部门信息不存在!");
            throw new ServiceException("新增账户信息操作 - 绑定部门账户关联信息时, 需绑定的部门信息不存在!");
        }
        // 数据转换 - 账户信息数据传输对象 转换为 数据实体对象
        Account account = accountMapper.accountDTOToAccount(accountDTO);
        // 密码进行加密处理
        account.setPassword(SaSecureUtil.md5(account.getPassword()));
        // 设置创建人、更新人信息
        account.setCreateBy(StpUtil.getLoginIdAsString());
        account.setUpdateBy(StpUtil.getLoginIdAsString());
        // 保存账户信息
        boolean saveResult = saveOrUpdate(account);
        // 判断保存结果是否成功
        if (!saveResult) {
            log.error("新增账户信息失败!");
            throw new ServiceException("新增账户信息失败!");
        }
        // 新增账户部门关联信息
        departmentAccountConnectionService.saveOneDepartmentAccountConnection(DepartmentAccountConnectionDTO.builder().departmentId(accountDTO.getDepartmentId()).accountId(account.getId()).tenantId(account.getTenantId()).build());
    }

    /**
     * 删除账户信息 - 使用 MyBatis-Plus 的 removeById 方法进行逻辑删除单条用户信息
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    @Transactional
    public void deleteOneAccount(AccountDTO accountDTO) {
        // 验证账户ID是否存在值
        verifyAccountParams(accountDTO, OperateTypeEnum.DELETE);
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getId, accountDTO.getId());
        Account account = this.accountRepository.selectOne(queryWrapper);
        if (StrUtil.isBlank(account.getId())) {
            log.error("根据账户 ID => {} , 未查询到对应的账户信息!", accountDTO.getId());
            throw new ServiceException("根据账户 ID => %s , 未查询到对应的账户信息!".formatted(accountDTO.getId()));
        }
        // 删除对应的账户部门关联信息
        Boolean deleteDepartmentAccountConnectionResult = departmentAccountConnectionService.deleteByAccountId(DepartmentAccountConnectionDTO.builder().accountId(account.getId()).build());
        if (!deleteDepartmentAccountConnectionResult) {
            log.error("删除账户信息 - 根据账户 ID => {} , 删除账户部门关联信息失败!", accountDTO.getId());
            throw new ServiceException("删除账户信息 - 根据账户 ID => %s , 删除账户部门关联信息失败!".formatted(accountDTO.getId()));
        }
        // 删除对应的账户角色关联信息
        Boolean deleteAccountRoleConnectionResult = accountRoleConnectionService.deleteByAccountId(AccountRoleConnectionDTO.builder().accountId(account.getId()).build());
        if (!deleteAccountRoleConnectionResult) {
            log.error("删除账户信息 - 根据账户 ID => {} , 删除账户角色关联信息失败!", accountDTO.getId());
            throw new ServiceException("删除账户信息 - 根据账户 ID => %s , 删除账户角色关联信息失败!".formatted(accountDTO.getId()));
        }
        // 设置账户的数据更新时间和更新人信息
        account.setUpdateBy(StpUtil.getLoginIdAsString());
        account.setUpdateTime(LocalDateTime.now());
        // 执行删除账户信息
        boolean deleteResult = this.removeById(account);
        if (!deleteResult) {
            log.error("在方法 deleteOneAccount 中, 删除账户信息失败!");
            throw new ServiceException("在方法 deleteOneAccount 中, 删除账户信息失败!");
        }
    }

    /**
     * 删除账户信息 - 批量删除账户信息数据
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAccount(AccountDTO accountDTO) {
        // 批量删除账户信息 - 验证账户数据主键 ID 集合列表
        verifyAccountParams(accountDTO, OperateTypeEnum.BATCH_DELETE);
        // 批量删除账户信息 - 根据账户 ID 集合列表, 批量删除部门账户关联信息
        Boolean deleteDepartmentAccountConnectionResult = departmentAccountConnectionService.deleteByAccountIds(DepartmentAccountConnectionDTO.builder().accountIds(accountDTO.getIds()).build());
        if (!deleteDepartmentAccountConnectionResult) {
            log.error("批量删除账户信息 - 根据账户 ID 集合列表, 批量删除部门账户关联信息失败!");
            throw new ServiceException("批量删除账户信息 - 根据账户 ID 集合列表, 批量删除部门账户关联信息失败!");
        }
        // 批量删除账户信息 - 根据账户 ID 集合列表, 批量删除账户角色关联信息
        Boolean deleteAccountRoleConnectionResult = accountRoleConnectionService.deleteByAccountIds(AccountRoleConnectionDTO.builder().accountIds(accountDTO.getIds()).build());
        if (!deleteAccountRoleConnectionResult) {
            log.error("批量删除账户信息 - 根据账户 ID 集合列表, 批量删除账户角色关联信息失败!");
            throw new ServiceException("批量删除账户信息 - 根据账户 ID 集合列表, 批量删除账户角色关联信息失败!");
        }
        // 批量删除账户信息 - 构建需要删除的账户信息实体对象列表, 手动设置更新时间、更新人信息
        List<Account> accountList = accountDTO.getIds().stream().map(id -> Account.builder().id(id).updateBy(StpUtil.getLoginIdAsString()).updateTime(LocalDateTime.now()).build()).collect(Collectors.toList());
        // 执行批量删除账户信息数据
        boolean deleteResult = removeByIds(accountList);
        // 判断删除结果
        if (!deleteResult) {
            log.error("根据账户 ID , 批量删除账户信息失败!");
            throw new ServiceException("根据账户 ID , 批量删除账户信息失败!");
        }
    }

    /**
     * 修改账户信息 - 修改单条账户信息数据
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneAccount(AccountDTO accountDTO) {
        // 验证账户ID是否存在值
        verifyAccountParams(accountDTO, OperateTypeEnum.UPDATE);
        Account account = accountRepository.selectOneAccountById(accountDTO.getId());
        if (Objects.isNull(account) || StrUtil.isBlank(account.getId())) {
            log.error("根据账户 ID => {} 更新账户数据, 未能查询到需要更新的账户信息!", accountDTO.getId());
            throw new ServiceException("根据账户 ID => %s 更新账户数据, 未能查询到需要更新的账户信息!".formatted(accountDTO.getId()));
        }
        if (StrUtil.isNotBlank(accountDTO.getUserName())) {
            Integer count = this.accountRepository.selectCountByUserNameEqAndIdNotEq(accountDTO.getUserName(), account.getId());
            if (count > 0) {
                log.error("根据账户 ID => {} 修改账户数据时, 账户用户名 => {} 已存在!", account.getId(), accountDTO.getUserName());
                throw new ServiceException("根据账户 ID => %s 修改账户数据时, 账户用户名 => %s 已存在!".formatted(account.getId(), accountDTO.getUserName()));
            }
            // 当用户名存在且系统内无相同的用户名时再进行用户名设置
            account.setUserName(accountDTO.getUserName());
        }
        // 当密码存在时再进行密码设置
        Optional.ofNullable(accountDTO.getPassword()).filter(StrUtil::isNotBlank).ifPresent(password -> account.setPassword(SaSecureUtil.md5(password)));
        // 当状态存在时再进行状态设置
        Optional.ofNullable(accountDTO.getStatus()).ifPresent(account::setStatus);
        // 当备注存在时再进行备注设置
        Optional.ofNullable(accountDTO.getRemark()).filter(StrUtil::isNotBlank).ifPresent(account::setRemark);
        account.setUpdateBy(StpUtil.getLoginIdAsString());
        account.setUpdateTime(LocalDateTime.now());
        // 执行修改操作
        boolean updateResult = this.saveOrUpdate(account);
        if (!updateResult) {
            log.error("修改账户信息失败!");
            throw new ServiceException("修改账户信息失败!");
        }
    }

    /**
     * 依据条件查询满足条件的一个账户信息 - 用户名 模糊匹配, 状态 精确匹配, 备注 模糊匹配
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
        // 执行查询
        IPage<Account> accountPage = page(page, queryWrapper);
        return PageUtils.buildPageResponse(accountPage, accountMapper.accountListToAccountDTOList(accountPage.getRecords()));
    }

    /**
     * 查询账户信息 - 依据用户名和密码查询账户信息
     *
     * @param accountDTO 账户信息查询参数封装对象 {@link AccountDTO}
     * @return 返回依据查询条件查询出的账户信息传输对象 {@link AccountDTO}
     */
    public AccountDTO queryOneAccountInfoByUserNameAndPassword(AccountDTO accountDTO) {
        // 验证用户名和密码是否存在值
        verifyAccountParams(accountDTO, OperateTypeEnum.QUERY);
        // 查询用户信息
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getUserName, accountDTO.getUserName());
        queryWrapper.eq(Account::getPassword, SaSecureUtil.md5(accountDTO.getPassword()));
        // 执行查询结果
        Account account = accountRepository.selectOne(queryWrapper);
        // 返回对应的账户信息数据传输对象
        return Optional.ofNullable(accountMapper.accountToAccountDTO(account)).orElse(AccountDTO.builder().build());
    }

    /**
     * 账户信息参数校验
     *
     * @param accountDTO  账户信息数据传输对象 {@link AccountDTO}
     * @param operateType 操作类型 {@link OperateTypeEnum}
     */
    private void verifyAccountParams(AccountDTO accountDTO, OperateTypeEnum operateType) {
        switch (operateType) {
            case SAVE -> verifySaveAccountParams(accountDTO);
            case DELETE -> verifyDeleteAccountParams(accountDTO);
            case BATCH_DELETE -> verifyBatchDeleteAccountParams(accountDTO);
            case UPDATE -> verifyUpdateAccountParams(accountDTO);
            case QUERY -> verifyQueryAccountParams(accountDTO);
            default -> throw new ServiceException("操作类型不存在!");
        }

    }

    /**
     * 账户信息新增参数校验 - 新增单条账户信息数据参数校验
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
     * 账户信息删除参数校验 - 删除单条账户信息数据参数校验
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    private void verifyDeleteAccountParams(AccountDTO accountDTO) {
        if (StrUtil.isBlank(accountDTO.getId())) {
            log.error("删除账户信息参数校验失败 - 账户ID不能为空!");
            throw new ServiceException("删除账户信息参数校验失败 - 账户ID不能为空!");
        }
    }

    /**
     * 账户信息删除参数校验 - 批量删除账户信息数据参数校验
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     */
    private void verifyBatchDeleteAccountParams(AccountDTO accountDTO) {
        if (CollectionUtil.isEmpty(accountDTO.getIds())) {
            log.error("批量删除账户信息 - 账户ID列表不能为空!");
            throw new ServiceException("批量删除账户信息 - 账户ID列表不能为空!");
        }
    }

    /**
     * 账户信息修改参数校验 - 修改单条账户信息数据参数校验
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
     * 账户信息查询参数校验 - 查询单条账户信息数据参数校验
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
