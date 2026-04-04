package com.ranyk.vt.boot.example.web.freamwork.service.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.DataStatusEnum;
import com.ranyk.vt.boot.base.constant.DefaultValueEnum;
import com.ranyk.vt.boot.base.constant.MqTypeEnum;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountUserConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.dto.UserDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.entity.User;
import com.ranyk.vt.boot.example.web.freamwork.mapper.user.UserMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.user.UserRepository;
import com.ranyk.vt.boot.example.web.freamwork.service.account.AccountUserConnectionService;
import com.ranyk.vt.boot.log.annotations.OperationRecord;
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
 * CLASS_NAME: UserService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息业务逻辑类
 * @date: 2026-03-17
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserRepository, User> {

    /**
     * 用户信息数据库操作接口对象
     */
    private final UserRepository userRepository;
    /**
     * 用户信息数据转换接口对象
     */
    private final UserMapper userMapper;
    /**
     * 账户用户关联关系业务逻辑类对象
     */
    private final AccountUserConnectionService accountUserConnectionService;

    /**
     * 构造函数 - 向 Spring IOC 容器中注册 UserRepository 用户信息数据库操作接口对象 UserMapper 用户信息数据转换接口对象 AccountUserConnectionService 账户用户关联关系业务逻辑类对象
     *
     * @param userRepository               用户信息数据库操作接口对象
     * @param userMapper                   用户信息数据转换接口对象
     * @param accountUserConnectionService 账户用户关联关系业务逻辑类对象
     */
    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       AccountUserConnectionService accountUserConnectionService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.accountUserConnectionService = accountUserConnectionService;
    }

    /**
     * 保存单个用户信息
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneUserInfo(UserDTO userDTO) {
        verifyUserParam(userDTO, OperateTypeEnum.SAVE);
        User user = userMapper.userDTOToUser(userDTO);
        boolean saveResult = this.saveOrUpdate(user);
        if (!saveResult) {
            log.error("保存单个用户信息失败!");
            throw new ServiceException("保存单个用户信息失败!");
        }
    }

    /**
     * 删除单个用户信息
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneUserInfo(UserDTO userDTO) {
        verifyUserParam(userDTO, OperateTypeEnum.DELETE);
        User user = userMapper.userDTOToUser(userDTO);
        boolean deleteResult = this.removeById(user);
        if (!deleteResult) {
            log.error("删除单个用户信息失败!");
            throw new ServiceException("删除单个用户信息失败!");
        }
    }

    /**
     * 批量删除用户信息
     *
     * @param userDTO 用户信息数据传输 DTO 列表, {@link UserDTO}
     */
    @SuppressWarnings("unused")
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchUserInfo(UserDTO userDTO) {
        verifyUserParam(userDTO, OperateTypeEnum.BATCH_DELETE);
        List<User> userList = userDTO.getIds().stream().map(id -> User.builder().id(id).updateBy(StpUtil.getLoginIdAsString()).updateTime(LocalDateTime.now()).build()).collect(Collectors.toList());
        boolean batchDeleteResult = this.removeBatchByIds(userList);
        if (!batchDeleteResult) {
            log.error("批量删除用户信息失败!");
            throw new ServiceException("批量删除用户信息失败!");
        }
    }

    /**
     * 更新单个用户信息
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneUserInfo(UserDTO userDTO) {
        verifyUserParam(userDTO, OperateTypeEnum.UPDATE);
        User user = this.userRepository.selectOneUserById(userDTO.getId());
        if (Objects.isNull(user) || StrUtil.isBlank(user.getId())) {
            log.error("更新单个用户信息失败,根据用户 ID => {} 更新用户信息数据, 未能查询出需要更新的用户信息!", userDTO.getId());
            throw new ServiceException("更新单个用户信息失败,根据用户 ID => %s 更新用户信息数据, 未能查询出需要更新的用户信息!".formatted(userDTO.getId()));
        }
        Optional.ofNullable(userDTO.getName()).filter(StrUtil::isNotBlank).ifPresent(user::setName);
        Optional.ofNullable(userDTO.getNickName()).filter(StrUtil::isNotBlank).ifPresent(user::setNickName);
        Optional.ofNullable(userDTO.getAvatar()).filter(StrUtil::isNotBlank).ifPresent(user::setAvatar);
        Optional.ofNullable(userDTO.getSex()).filter(sex -> sex > 0).ifPresent(user::setSex);
        Optional.ofNullable(userDTO.getEmail()).filter(StrUtil::isNotBlank).ifPresent(user::setEmail);
        Optional.ofNullable(userDTO.getPhone()).filter(StrUtil::isNotBlank).ifPresent(user::setPhone);
        Optional.ofNullable(userDTO.getStatus()).filter(status -> status > 0).ifPresent(user::setStatus);
        Optional.ofNullable(userDTO.getRemark()).filter(StrUtil::isNotBlank).ifPresent(user::setRemark);
        user.setUpdateBy(StpUtil.getLoginIdAsString());
        user.setUpdateTime(LocalDateTime.now());
        boolean updateResult = this.saveOrUpdate(user);
        if (!updateResult) {
            log.error("更新单个用户信息失败!");
            throw new ServiceException("更新单个用户信息失败!");
        }
    }

    /**
     * 查询用户信息
     *
     * @param userDTO 用户信息数据传输 DTO 列表, {@link UserDTO}
     * @return 分页查询对象 {@link PageResponse} - 用户信息数据传输 DTO {@link UserDTO} 对象列表
     */
    public PageResponse<UserDTO> queryUserInfoByConditions(UserDTO userDTO) {
        Page<User> page = PageUtils.buildPage(userDTO);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(userDTO.getName()), User::getName, userDTO.getName());
        queryWrapper.like(StrUtil.isNotBlank(userDTO.getNickName()), User::getNickName, userDTO.getNickName());
        queryWrapper.eq(Objects.nonNull(userDTO.getSex()) && userDTO.getSex() > 0 && userDTO.getSex() < 4, User::getSex, userDTO.getSex());
        queryWrapper.like(StrUtil.isNotBlank(userDTO.getEmail()), User::getEmail, userDTO.getEmail());
        queryWrapper.like(StrUtil.isNotBlank(userDTO.getPhone()), User::getPhone, userDTO.getPhone());
        queryWrapper.eq(Objects.nonNull(userDTO.getStatus()) && userDTO.getStatus() >= -2 && userDTO.getStatus() < 2, User::getStatus, userDTO.getStatus());
        queryWrapper.like(StrUtil.isNotBlank(userDTO.getRemark()), User::getRemark, userDTO.getRemark());
        Page<User> userPage = this.page(page, queryWrapper);
        return PageUtils.buildPageResponse(userPage, userMapper.userListToUserDTOList(userPage.getRecords()));

    }

    /**
     * 查询当前登录用户信息
     *
     * @return 当前登录用户信息查询结果数据传输 DTO 对象 {@link UserDTO}
     */
    public UserDTO queryCurrentUserInfo() {
        return queryByAccountId(StpUtil.getLoginIdAsString());
    }

    /**
     * 根据账户 ID 查询用户信息
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    public UserDTO queryUserInfoByAccountId(UserDTO userDTO) {
        return queryByAccountId(userDTO.getAccountId());
    }

    /**
     * 根据账户 ID 查询用户信息
     *
     * @param accountId 账户 ID
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    private UserDTO queryByAccountId(String accountId) {
        AccountUserConnectionDTO accountUserConnectionDTO = accountUserConnectionService.queryByAccountId(AccountUserConnectionDTO.builder().accountId(accountId).build());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, accountUserConnectionDTO.getUserId());
        User user = userRepository.selectOne(queryWrapper);
        if (Objects.isNull(user) || StrUtil.isBlank(user.getId())) {
            log.error("根据账户 ID 查询用户信息失败, 未查询出用户信息!");
            throw new ServiceException("根据账户 ID 查询用户信息失败, 未查询出用户信息!");
        }
        return userMapper.userToUserDTO(user);
    }

    /**
     * 绑定账户用户关系
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    @OperationRecord(desc = "绑定账户用户关系",
            type = OperateTypeEnum.SAVE,
            isSaveOperationRecord = true,
            saveOperationRecordMethod = OperationRecord.SaveMethod.ASYNC,
            asyncMethod = MqTypeEnum.RABBITMQ)
    public void bundledAccountUser(UserDTO userDTO) {
        Boolean bundledResult = accountUserConnectionService.saveOneAccountUserConnectionInfo(AccountUserConnectionDTO.builder().accountId(userDTO.getAccountId()).userId(userDTO.getId()).build());
        if (!bundledResult) {
            log.error("绑定账户用户关系失败!");
            throw new ServiceException("绑定账户用户关系失败!");
        }
    }

    /**
     * 解绑账户用户关系
     *
     * @param userDTO 用户信息数据传输 DTO 列表, {@link UserDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    @OperationRecord(desc = "解绑账户用户关系",
            type = OperateTypeEnum.DELETE,
            isSaveOperationRecord = true,
            saveOperationRecordMethod = OperationRecord.SaveMethod.ASYNC,
            asyncMethod = MqTypeEnum.RABBITMQ)
    public void unbundledAccountUser(UserDTO userDTO) {
        Boolean deleteResult;
        if (StrUtil.isNotBlank(userDTO.getAccountUserConnectionId())) {
            deleteResult = accountUserConnectionService.deleteOneAccountUserConnectionInfoById(AccountUserConnectionDTO.builder().id(userDTO.getAccountUserConnectionId()).build());
        } else {
            deleteResult = accountUserConnectionService.deleteOneAccountUserConnectionInfoByAccountIdAndUserId(AccountUserConnectionDTO.builder().accountId(userDTO.getAccountId()).userId(userDTO.getId()).build());
        }
        if (!deleteResult) {
            log.error("解绑账户用户关系失败!");
            throw new ServiceException("解绑账户用户关系失败!");
        }
    }

    /**
     * 查询账户用户关联关系信息
     *
     * @param userDTO 账户用户关联关系查询条件数据封装对象, 当前使用的是 {@link UserDTO#getName()} 和 {@link UserDTO#getAccountName()} 两个属性
     * @return 账户用户关联关系信息查询结果数据传输 DTO 列表, {@link AccountUserConnectionDTO}
     */
    public PageResponse<AccountUserConnectionDTO> queryAccountUserConnectionInfo(UserDTO userDTO) {
        Page<AccountUserConnectionDTO> page = PageUtils.buildPage(userDTO);
        IPage<AccountUserConnectionDTO> accountUserConnectionDTOPage = this.userRepository.selectByUserNameLikeAndAccountNameLike(page, userDTO.getName(), userDTO.getAccountName());
        return PageUtils.buildPageResponse(accountUserConnectionDTOPage, accountUserConnectionDTOPage.getRecords());
    }

    /**
     * 参数校验
     *
     * @param userDTO     用户信息数据传输 DTO 对象, {@link UserDTO}
     * @param operateType 操作类型, {@link OperateTypeEnum}
     */
    private void verifyUserParam(UserDTO userDTO, OperateTypeEnum operateType) {
        switch (operateType) {
            case SAVE -> verifySaveUserParam(userDTO);
            case DELETE -> verifyDeleteUserParam(userDTO);
            case BATCH_DELETE -> verifyBatchDeleteUserParam(userDTO);
            case UPDATE -> verifyUpdateUserParam(userDTO);
            default -> throw new ServiceException("不支持的操作类型!");
        }
    }

    /**
     * 参数校验 - 保存单个用户信息参数校验, 用户名不能为空, 其他数据如果为空则设置为默认值
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    private void verifySaveUserParam(UserDTO userDTO) {
        if (StrUtil.isBlank(userDTO.getName())) {
            log.error("保存用户信息失败, 用户名不能为空!");
            throw new ServiceException("保存用户信息失败, 用户名不能为空!");
        }
        if (StrUtil.isBlank(userDTO.getNickName())) {
            log.error("保存用户信息时, 昵称为空, 将昵称设置为用户名!");
            userDTO.setNickName(userDTO.getName());
        }
        if (Objects.isNull(userDTO.getSex())) {
            log.error("保存用户信息时, 性别为空, 将性别设置为默认值!");
            userDTO.setSex(Integer.parseInt(DefaultValueEnum.SEX_DEFAULT.getValue()));
        }
        if (StrUtil.isBlank(userDTO.getAvatar())) {
            log.error("保存用户信息时, 头像为空, 将头像设置为默认值!");
            userDTO.setAvatar(DefaultValueEnum.STR_DEFAULT.getValue());
        }
        if (StrUtil.isBlank(userDTO.getEmail())) {
            log.error("保存用户信息时, 邮箱为空, 将邮箱设置为默认值!");
            userDTO.setEmail(DefaultValueEnum.STR_DEFAULT.getValue());
        }
        if (StrUtil.isBlank(userDTO.getPhone())) {
            log.error("保存用户信息时, 手机号码为空, 将手机号码设置为默认值!");
            userDTO.setPhone(DefaultValueEnum.STR_DEFAULT.getValue());
        }
        userDTO.setStatus(DataStatusEnum.NORMAL.getValue());
        userDTO.setCreateBy(StpUtil.getLoginIdAsString());
        userDTO.setUpdateBy(StpUtil.getLoginIdAsString());
    }

    /**
     * 参数校验 - 删除单个用户信息参数校验, 用户 ID 不能为空
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    private void verifyDeleteUserParam(UserDTO userDTO) {
        if (StrUtil.isBlank(userDTO.getId())) {
            log.error("删除单个用户信息失败, 用户 ID 不能为空!");
            throw new ServiceException("删除单个用户信息失败, 用户 ID 不能为空!");
        }
        userDTO.setUpdateBy(StpUtil.getLoginIdAsString());
        userDTO.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 参数校验 - 批量删除用户信息参数校验, 用户 ID 列表不能为空
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    private void verifyBatchDeleteUserParam(UserDTO userDTO) {
        if (CollUtil.isEmpty(userDTO.getIds())) {
            log.error("删除多个用户信息失败, 用户 ID 列表不能为空!");
            throw new ServiceException("删除多个用户信息失败, 用户 ID 列表不能为空!");
        }
    }

    /**
     * 参数校验 - 更新单个用户信息参数校验, 用户 ID 不能为空
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    private void verifyUpdateUserParam(UserDTO userDTO) {
        if (StrUtil.isBlank(userDTO.getId())) {
            log.error("更新单个用户信息失败, 用户 ID 不能为空!");
            throw new ServiceException("更新单个用户信息失败, 用户 ID 不能为空!");
        }
        userDTO.setUpdateBy(StpUtil.getLoginIdAsString());
        userDTO.setUpdateTime(LocalDateTime.now());
    }

}
