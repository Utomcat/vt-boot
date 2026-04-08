package com.ranyk.vt.boot.example.web.freamwork.mapper.account;


import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountUserConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.Account;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountRoleConnection;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountUserConnection;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.po.*;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.vo.LoginAccountVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.vo.QueryAccountVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * CLASS_NAME: AccountMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户信息数据对象转换映射 Mapper 接口
 * @date: 2026-03-01
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

    /**
     * 将 新增账户请求参数封装 PO 类 转换为 账户数据传输对象
     *
     * @param saveAccountPO 新增账户请求参数封装 PO 类 {@link SaveAccountPO}
     * @return 账户数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    AccountDTO saveAccountPOToAccountDTO(SaveAccountPO saveAccountPO);

    /**
     * 将 删除账户请求参数封装 PO 类 转换为 账户数据传输对象
     *
     * @param deleteAccountPO 删除账户请求参数封装 PO 类 {@link DeleteAccountPO}
     * @return 账户数据传输对象 {@link AccountDTO}
     */

    @Mappings({
            @Mapping(target = "userName", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "departmentId", ignore = true),
    })
    AccountDTO deleteAccountPOToAccountDTO(DeleteAccountPO deleteAccountPO);

    /**
     * 将 修改账户请求参数封装 PO 类 转换为 账户数据传输对象
     *
     * @param updateAccountPO 修改账户请求参数封装 PO 类 {@link UpdateAccountPO}
     * @return 账户数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "departmentId", ignore = true),
    })
    AccountDTO updateAccountPOToAccountDTO(UpdateAccountPO updateAccountPO);

    /**
     * 将 账户查询请求参数封装 PO 类 转换为 账户数据传输对象
     *
     * @param queryAccountPO 账户查询请求参数封装 PO 类 {@link QueryAccountPO}
     * @return 账户数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    AccountDTO queryAccountPOToAccountDTO(QueryAccountPO queryAccountPO);

    /**
     * 将 账户登录请求参数封装 PO 类 转换为 账户数据传输对象
     *
     * @param loginAccountPO 账户登录请求参数封装 PO 类 {@link LoginAccountPO}
     * @return 账户数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "departmentId", ignore = true),
    })
    AccountDTO loginAccountPOToAccountDTO(LoginAccountPO loginAccountPO);

    /**
     * 将 账户数据传输对象 转换为 账户数据实体类
     *
     * @param accountDTO 账户数据传输对象 {@link AccountDTO}
     * @return 账户数据实体类 {@link Account}
     */
    Account accountDTOToAccount(AccountDTO accountDTO);

    /**
     * 将 账户数据实体类 转换为 账户数据传输对象
     *
     * @param account 账户数据实体类 {@link Account}
     * @return 账户数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "departmentId", ignore = true),
    })
    AccountDTO accountToAccountDTO(Account account);

    /**
     * 将 账户数据实体类 列表转换为 账户数据传输对象 列表
     *
     * @param accountList 账户数据实体类 列表 {@link Account}
     * @return 账户数据传输对象 列表 {@link AccountDTO}
     */
    List<AccountDTO> accountListToAccountDTOList(List<Account> accountList);

    /**
     * 将 账户数据传输对象 转换为 账户查询结果数据传输对象
     *
     * @param accountDTO 账户数据传输对象 {@link AccountDTO}
     * @return 账户查询结果数据视图对象 {@link QueryAccountVO}
     */
    QueryAccountVO accountDTOToQueryAccountVO(AccountDTO accountDTO);

    /**
     * 将 账户数据传输对象 转换为 账户登录结果数据视图对象
     *
     * @param accountDTO 账户数据传输对象 {@link AccountDTO}
     * @return 账户登录结果数据视图对象 {@link LoginAccountVO}
     */
    LoginAccountVO accountDTOToLoginAccountVO(AccountDTO accountDTO);

    /**
     * 将 账户数据传输对象 列表转换为 账户查询结果数据传输对象 列表
     *
     * @param accountDTOList 账户数据传输对象 列表 {@link AccountDTO}
     * @return 账户查询结果数据视图对象 列表 {@link QueryAccountVO}
     */
    List<QueryAccountVO> accountDTOListToQueryAccountVOList(List<AccountDTO> accountDTOList);

    /**
     * 将 账户角色关联关系数据传输对象 转换为 账户角色关联关系数据实体类
     *
     * @param accountRoleConnectionDTO 账户角色关联关系数据传输对象 {@link AccountRoleConnectionDTO}
     * @return 账户角色关联关系数据实体类 {@link AccountRoleConnection}
     */
    AccountRoleConnection accountRoleConnectionDTOToAccountRoleConnection(AccountRoleConnectionDTO accountRoleConnectionDTO);

    /**
     * 将 账户角色关联关系数据实体类 转换为 账户角色关联关系数据传输对象
     *
     * @param accountRoleConnection 账户角色关联关系数据实体类 {@link AccountRoleConnection}
     * @return 账户角色关联关系数据传输对象 {@link AccountRoleConnectionDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountIds", ignore = true),
            @Mapping(target = "roleIds", ignore = true),
    })
    AccountRoleConnectionDTO accountRoleConnectionToAccountRoleConnectionDTO(AccountRoleConnection accountRoleConnection);

    /**
     * 将 账户角色关联关系数据实体类 列表转换为 账户角色关联关系数据传输对象 列表
     *
     * @param accountRoleConnectionList 账户角色关联关系数据实体类 列表 {@link AccountRoleConnection}
     * @return 账户角色关联关系数据传输对象 列表 {@link AccountRoleConnectionDTO}
     */
    List<AccountRoleConnectionDTO> accountRoleConnectionListToAccountRoleConnectionDTOList(List<AccountRoleConnection> accountRoleConnectionList);

    /**
     * 将 账户用户关联关系数据传输 DTO 对象 转换为 账户用户关联关系数据实体类 对象
     *
     * @param accountUserConnectionDTO 账户用户关联关系数据传输 DTO 对象, {@link AccountUserConnectionDTO}
     * @return 账户用户关联关系数据实体类 对象, {@link AccountUserConnection}
     */
    AccountUserConnection accountUserConnectionDTOToAccountUserConnection(AccountUserConnectionDTO accountUserConnectionDTO);

    /**
     * 将 账户用户关联关系数据实体类 转换为 账户用户关联关系数据传输 DTO 对象
     *
     * @param accountUserConnection 账户用户关联关系数据实体类 {@link AccountUserConnection}
     * @return 账户用户关联关系数据传输 DTO 对象 {@link AccountUserConnectionDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "accountName", ignore = true),
            @Mapping(target = "userName", ignore = true),
    })
    AccountUserConnectionDTO accountUserConnectionToAccountUserConnectionDTO(AccountUserConnection accountUserConnection);

    /**
     * 将 账户用户关联关系 PO类 转换为 账户用户关联关系数据传输 DTO 列表
     *
     * @param accountBundledRolePO 账户用户关联关系 PO 类 {@link AccountBundledRolePO}
     * @return 账户用户关联关系数据传输 DTO 列表 {@link AccountRoleConnectionDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "accountId", ignore = true),
            @Mapping(target = "roleId", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    AccountRoleConnectionDTO accountBundledRolePOToAccountRoleConnectionDTO(AccountBundledRolePO accountBundledRolePO);

    /**
     * 将 账户用户关联关系数据传输 DTO 列表转换为 账户用户关联关系数据实体类 列表
     *
     * @param accountRoleConnectionDTOS 账户用户关联关系数据传输 DTO 列表 {@link AccountRoleConnectionDTO}
     * @return 账户用户关联关系数据实体类 列表 {@link AccountRoleConnection}
     */
    List<AccountRoleConnection> accountRoleConnectionDTOListToAccountRoleConnectionList(List<AccountRoleConnectionDTO> accountRoleConnectionDTOS);
}
