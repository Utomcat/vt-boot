package com.ranyk.vt.boot.example.web.freamwork.mapper.user;

import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountUserConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.dto.UserDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.entity.User;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.po.*;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.vo.QueryAccountUserConnectionVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.vo.QueryUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * CLASS_NAME: UserMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息数据转换接口
 * @date: 2026-03-17
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * 将 用户信息保存参数 PO 映射为 用户信息数据传输 DTO 对象
     *
     * @param saveUserPO 用户信息保存参数 PO, {@link SaveUserPO}
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO saveUserPOToUserDTO(SaveUserPO saveUserPO);

    /**
     * 将 用户信息删除参数 PO 映射为 用户信息数据传输 DTO 对象
     *
     * @param deleteUserPO 用户信息删除参数 PO, {@link DeleteUserPO}
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "nickName", ignore = true),
            @Mapping(target = "sex", ignore = true),
            @Mapping(target = "avatar", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO deleteUserPOToUserDTO(DeleteUserPO deleteUserPO);

    /**
     * 将 用户信息修改参数 PO 映射为 用户信息数据传输 DTO 对象
     *
     * @param updateUserPO 用户信息修改参数 PO, {@link UpdateUserPO}
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO updateUserPOToUserDTO(UpdateUserPO updateUserPO);

    /**
     * 将 通过账户ID查询用户信息参数 PO 对象 映射为 用户信息数据传输 DTO 对象
     *
     * @param queryUserPO 通过账户ID查询用户信息参数 PO, {@link QueryUserPO}
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "avatar", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO queryUserPOToUserDTO(QueryUserPO queryUserPO);

    /**
     * 将 通过账户ID查询用户信息参数 PO 映射为 用户信息数据传输 DTO 对象
     *
     * @param queryUserByAccountIdPO 通过账户ID查询用户信息参数 PO, {@link QueryUserByAccountIdPO}
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "nickName", ignore = true),
            @Mapping(target = "sex", ignore = true),
            @Mapping(target = "avatar", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO queryUserByAccountIdPOToUserDTO(QueryUserByAccountIdPO queryUserByAccountIdPO);

    /**
     * 将 用户信息数据传输 DTO 映射为 用户信息实体对象
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     * @return 用户信息实体对象, {@link User}
     */
    User userDTOToUser(UserDTO userDTO);

    /**
     * 将 用户信息实体对象 转换为 用户信息数据传输 DTO 对象
     *
     * @param user 用户信息实体对象, {@link User}
     * @return 用户信息数据传输 DTO 对象, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO userToUserDTO(User user);

    /**
     * 将 用户信息数据传输 DTO 对象 转换为 用户信息查询结果视图 VO 对象
     *
     * @param userDTO 用户信息数据传输 DTO 对象, {@link UserDTO}
     * @return 用户信息查询结果视图 VO 对象, {@link QueryUserVO}
     */
    QueryUserVO userDTOToQueryUserVO(UserDTO userDTO);

    /**
     * 将 用户信息数据传输 DTO 对象列表 转换为 用户信息查询结果视图 VO 对象列表
     *
     * @param userDTOList 用户信息数据传输 DTO 对象列表, {@link List<UserDTO>}
     * @return 用户信息查询结果视图 VO 对象列表, {@link List<QueryUserVO>}
     */
    List<QueryUserVO> userDTOListToQueryUserVOList(List<UserDTO> userDTOList);

    /**
     * 将 用户信息实体对象列表 映射为 用户信息数据传输 DTO 列表
     *
     * @param userList 用户信息实体对象列表 - {@link User}
     * @return 用户信息数据传输 DTO 列表 - {@link UserDTO}
     */
    List<UserDTO> userListToUserDTOList(List<User> userList);

    /**
     * 将 绑定用户参数 PO 映射为 用户信息数据传输 DTO 列表
     *
     * @param bundledAccountUserPO 绑定用户参数 PO, {@link BundledAccountUserPO}
     * @return 用户信息数据传输 DTO 列表, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "nickName", ignore = true),
            @Mapping(target = "sex", ignore = true),
            @Mapping(target = "avatar", ignore = true),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO bundledAccountUserPOToUserDTO(BundledAccountUserPO bundledAccountUserPO);

    /**
     * 将 解绑用户参数 PO 映射为 用户信息数据传输 DTO 列表
     *
     * @param unbundledAccountUserPO 解绑用户参数 PO, {@link UnbundledAccountUserPO}
     * @return 用户信息数据传输 DTO 列表, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "id", source = "userId"),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "nickName", ignore = true),
            @Mapping(target = "sex", ignore = true),
            @Mapping(target = "avatar", ignore = true),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "accountUserConnectionId", source = "id"),
    })
    UserDTO unbundledAccountUserPOToUserDTO(UnbundledAccountUserPO unbundledAccountUserPO);

    /**
     * 将 账户用户关联关系查询参数 PO 映射为 用户信息数据传输 DTO
     *
     * @param queryAccountUserConnectionPO 账户用户关联关系查询参数 PO, {@link QueryAccountUserConnectionPO}
     * @return 用户信息数据传输 DTO, {@link UserDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", source = "userName"),
            @Mapping(target = "nickName", ignore = true),
            @Mapping(target = "sex", ignore = true),
            @Mapping(target = "avatar", ignore = true),
            @Mapping(target = "phone", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "accountUserConnectionId", ignore = true),
    })
    UserDTO queryAccountUserConnectionPOToUserDTO(QueryAccountUserConnectionPO queryAccountUserConnectionPO);

    /**
     * 将 账户用户关联关系数据传输 DTO 列表 映射为 账户用户关联关系查询结果视图 VO 列表
     *
     * @param accountUserConnectionDTOList 账户用户关联关系数据传输 DTO 列表, {@link List<AccountUserConnectionDTO>}
     * @return 账户用户关联关系查询结果视图 VO 列表, {@link List<QueryAccountUserConnectionVO>}
     */
    List<QueryAccountUserConnectionVO> accountUserConnectionDTOListToQueryAccountUserConnectionVOList(List<AccountUserConnectionDTO> accountUserConnectionDTOList);
}
