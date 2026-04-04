package com.ranyk.vt.boot.example.web.freamwork.api.user;

import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountUserConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.dto.UserDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.po.*;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.vo.QueryAccountUserConnectionVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.vo.QueryUserVO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.user.UserMapper;
import com.ranyk.vt.boot.example.web.freamwork.service.user.UserService;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CLASS_NAME: UserApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息API接口
 * @date: 2026-03-17
 */
@RestController
@RequestMapping("/api/user")
public class UserApi {

    /**
     * 用户信息业务逻辑类对象
     */
    private final UserService userService;
    /**
     * 用户信息数据转换接口对象
     */
    private final UserMapper userMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入 UserService 对象
     *
     * @param userService 用户信息业务逻辑类对象, {@link UserService}
     * @param userMapper  用户信息数据转换接口对象, {@link UserMapper}
     */
    @Autowired
    public UserApi(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * 保存单条用户信息
     *
     * @param saveUserPO 保存用户信息请求数据封装 PO 类, {@link SaveUserPO}
     * @return 保存结果
     */
    @PostMapping
    public Result<Boolean> saveOneUserInfo(@RequestBody SaveUserPO saveUserPO) {
        userService.saveOneUserInfo(userMapper.saveUserPOToUserDTO(saveUserPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除单条用户信息
     *
     * @param deleteUserPO 删除用户信息请求数据封装 PO 类, {@link DeleteUserPO}
     * @return 删除结果
     */
    @DeleteMapping
    public Result<Boolean> deleteOneUserInfo(@RequestBody DeleteUserPO deleteUserPO) {
        userService.deleteOneUserInfo(userMapper.deleteUserPOToUserDTO(deleteUserPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 修改单条用户信息
     *
     * @param updateUserPO 修改用户信息请求数据封装 PO 类, {@link UpdateUserPO}
     * @return 修改结果
     */
    @PutMapping
    public Result<Boolean> updateUserInfo(@RequestBody UpdateUserPO updateUserPO) {
        userService.updateOneUserInfo(userMapper.updateUserPOToUserDTO(updateUserPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询用户信息
     *
     * @param queryUserPO 查询用户信息请求数据封装 PO 类, {@link QueryUserPO}
     * @return 用户信息查询结果视图 VO 列表 {@link QueryUserVO}
     */
    @GetMapping
    public MultiResult<QueryUserVO> queryUserInfo(QueryUserPO queryUserPO) {
        PageResponse<UserDTO> pageResponse = userService.queryUserInfoByConditions(userMapper.queryUserPOToUserDTO(queryUserPO));
        return MultiResult.successMulti(userMapper.userDTOListToQueryUserVOList(pageResponse.getData()), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
    }

    /**
     * 查询当前登录用户信息
     *
     * @return 当前登录用户信息查询结果视图 VO 对象 {@link QueryUserVO}
     */
    @GetMapping("/current")
    public QueryUserVO queryCurrentUserInfo() {
        UserDTO userDTO = userService.queryCurrentUserInfo();
        return userMapper.userDTOToQueryUserVO(userDTO);
    }

    /**
     * 通过账户ID查询用户信息
     *
     * @param queryUserByAccountIdPO 查询用户信息请求数据封装 PO 类, {@link QueryUserByAccountIdPO}
     * @return 用户信息查询结果视图 VO 对象 {@link QueryUserVO}
     */
    @GetMapping("/by/account/id")
    public QueryUserVO queryUserInfoByAccountId(QueryUserByAccountIdPO queryUserByAccountIdPO) {
        UserDTO userDTO = userService.queryUserInfoByAccountId(userMapper.queryUserByAccountIdPOToUserDTO(queryUserByAccountIdPO));
        return userMapper.userDTOToQueryUserVO(userDTO);
    }

    /**
     * 绑定账户用户关系
     *
     * @param bundledAccountUserPO 绑定账户用户关系请求数据封装 PO 类, {@link BundledAccountUserPO}
     * @return 绑定账户用户关系结果
     */
    @PostMapping("/bundled/account")
    public Result<Boolean> bundledAccountUser(@RequestBody BundledAccountUserPO bundledAccountUserPO) {
        userService.bundledAccountUser(userMapper.bundledAccountUserPOToUserDTO(bundledAccountUserPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 解绑账户用户关系
     *
     * @param unbundledAccountUserPO 解绑账户用户关系请求数据封装 PO 类, {@link UnbundledAccountUserPO}
     * @return 解绑账户用户关系结果
     */
    @DeleteMapping("/unbundled/account")
    public Result<Boolean> unbundledAccountUser(@RequestBody UnbundledAccountUserPO unbundledAccountUserPO) {
        userService.unbundledAccountUser(userMapper.unbundledAccountUserPOToUserDTO(unbundledAccountUserPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询账户用户关系
     *
     * @param queryAccountUserConnectionPO 查询账户用户关系请求数据封装 PO 类, {@link QueryAccountUserConnectionPO}
     * @return 账户用户关系查询结果视图 VO 列表 {@link QueryAccountUserConnectionVO}
     */
    @GetMapping("/account/connection")
    public MultiResult<QueryAccountUserConnectionVO> queryAccountUserConnection(QueryAccountUserConnectionPO queryAccountUserConnectionPO) {
        PageResponse<AccountUserConnectionDTO> pageResponse = userService.queryAccountUserConnectionInfo(userMapper.queryAccountUserConnectionPOToUserDTO(queryAccountUserConnectionPO));
        return MultiResult.successMulti(userMapper.accountUserConnectionDTOListToQueryAccountUserConnectionVOList(pageResponse.getData()), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
    }
}
