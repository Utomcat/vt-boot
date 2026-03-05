package com.ranyk.vt.boot.example.satoken.mapper.account;

import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.Account;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.AccountRoleConnection;
import com.ranyk.vt.boot.example.satoken.domain.account.po.*;
import com.ranyk.vt.boot.example.satoken.domain.account.vo.LoginAccountVO;
import com.ranyk.vt.boot.example.satoken.domain.account.vo.QueryAccountVO;
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
     * 登录请求参数数据对象转换成数据传输对象
     *
     * @param loginAccountPO 登录请求参数数据对象 {@link LoginAccountPO}
     * @return 登录信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    AccountDTO loginRequestPOToDTO(LoginAccountPO loginAccountPO);

    /**
     * 保存请求参数数据转换成数据传输对象
     *
     * @param saveAccountPO 数据保存参数数据对象 {@link SaveAccountPO}
     * @return 账户信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    AccountDTO saveRequestPOToDTO(SaveAccountPO saveAccountPO);

    /**
     * 删除请求参数数据转换成数据传输对象
     *
     * @param deleteAccountPO 数据删除参数数据对象 {@link DeleteAccountPO}
     * @return 账户信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "userName", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    AccountDTO deleteRequestPOToDTO(DeleteAccountPO deleteAccountPO);

    /**
     * 更新请求参数数据转换成数据传输对象
     *
     * @param updateAccountPO 更新请求参数数据对象 {@link UpdateAccountPO}
     * @return 账户信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    AccountDTO updateRequestPOToDTO(UpdateAccountPO updateAccountPO);

    /**
     * 查询请求参数数据转换成数据传输对象
     *
     * @param queryAccountPO 查询请求参数数据对象 {@link QueryAccountPO}
     * @return 账户信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    AccountDTO queryRequestPOToDTO(QueryAccountPO queryAccountPO);

    /**
     * 数据传输对象转换成数据实体对象
     *
     * @param account 登录信息数据传输对象 {@link Account}
     * @return 账户信息数据实体对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(target = "captcha", ignore = true),
            @Mapping(target = "captchaKey", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    AccountDTO entityToDTO(Account account);

    /**
     * 数据传输对象 转换为 数据实体对象
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     * @return 账户信息数据实体对象 {@link Account}
     */
    Account dtoToEntity(AccountDTO accountDTO);

    /**
     * 数据传输对象转换成数据视图对象
     *
     * @param accountDTO 登录信息数据传输对象 {@link AccountDTO}
     * @return 账户信息数据视图对象 {@link LoginAccountVO}
     */
    @Mappings({
            @Mapping(source = "token", target = "token"),
    })
    LoginAccountVO dtoToLoginAccountVO(AccountDTO accountDTO);

    /**
     * 数据实体对象 List 集合转换成 数据传输对象 List 集合
     *
     * @param accountRoleConnections 数据实体对象 {@link AccountRoleConnection} 的 List 集合
     * @return 数据传输对象 {@link AccountRoleConnectionDTO} 的 List 集合
     */
    List<AccountRoleConnectionDTO> accountRoleConnectionEntityListToDTO(List<AccountRoleConnection> accountRoleConnections);

    /**
     * 数据实体对象 List 集合转换成 数据传输对象 List 集合
     *
     * @param accounts 数据实体对象 {@link Account} 的 List 集合
     * @return 数据传输对象 {@link AccountDTO} 的 List 集合
     */
    List<AccountDTO> accountEntityListToDTO(List<Account> accounts);

    /**
     * 数据传输对象 List 集合转换成 数据视图对象 List 集合
     *
     * @param accountDTOList 数据传输对象 {@link AccountDTO} 的 List 集合
     * @return 数据视图对象 {@link QueryAccountVO} 的 List 集合
     */
    List<QueryAccountVO> accountDTOListToQueryAccountVOList(List<AccountDTO> accountDTOList);
}
