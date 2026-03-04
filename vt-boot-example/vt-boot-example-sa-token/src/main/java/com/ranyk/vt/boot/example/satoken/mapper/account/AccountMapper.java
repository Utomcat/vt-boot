package com.ranyk.vt.boot.example.satoken.mapper.account;

import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountRoleConnectionDTO;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.Account;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.AccountRoleConnection;
import com.ranyk.vt.boot.example.satoken.domain.account.po.AccountPO;
import com.ranyk.vt.boot.example.satoken.domain.account.vo.AccountVO;
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
     * @param accountPO 登录请求参数数据对象 {@link AccountPO}
     * @return 登录信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "captchaKey", target = "captchaKey"),
            @Mapping(source = "captcha", target = "captcha"),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true)
    })
    AccountDTO loginRequestPOToDTO(AccountPO accountPO);

    /**
     * 保存请求参数数据转换成数据传输对象
     *
     * @param accountPO 数据保存参数数据对象 {@link AccountPO}
     * @return 账户信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true)
    })
    AccountDTO saveRequestPOToDTO(AccountPO accountPO);

    /**
     * 删除请求参数数据转换成数据传输对象
     *
     * @param accountPO 数据删除参数数据对象 {@link AccountPO}
     * @return 账户信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true)
    })
    AccountDTO deleteRequestPOToDTO(AccountPO accountPO);

    /**
     * 更新请求参数数据转换成数据传输对象
     *
     * @param accountPO 更新请求参数数据对象 {@link AccountPO}
     * @return 账户信息数据传输对象 {@link AccountDTO}
     */
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "token", ignore = true),
            @Mapping(target = "tenantId", ignore = true)
    })
    AccountDTO updateRequestPOToDTO(AccountPO accountPO);

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
            @Mapping(target = "token", ignore = true)
    })
    AccountDTO entityToDTO(Account account);

    /**
     * 保存账户信息 - 数据传输对象 转换为 数据实体对象
     *
     * @param accountDTO 账户信息数据传输对象 {@link AccountDTO}
     * @return 账户信息数据实体对象 {@link Account}
     */
    @Mappings({
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "password", target = "password"),
            @Mapping(target = "tenantId", source = "tenantId", defaultValue = "0000000001")
    })
    Account saveDTOToEntity(AccountDTO accountDTO);

    /**
     * 删除账户信息 - 数据传输对象 转换为 数据实体对象
     *
     * @param accountDTO 账号信息数据传输对象 {@link AccountDTO}
     * @return 账户信息数据实体对象 {@link Account}
     */
    @Mappings({
            @Mapping(target = "id", source = "id")
    })
    Account deleteDTOToEntity(AccountDTO accountDTO);

    /**
     * 数据传输对象转换成数据视图对象
     *
     * @param accountDTO 登录信息数据传输对象 {@link AccountDTO}
     * @return 账户信息数据视图对象 {@link AccountVO}
     */
    @Mappings({
            @Mapping(source = "token", target = "token"),
    })
    AccountVO dtoToVO(AccountDTO accountDTO);

    /**
     * 数据实体对象 List 集合转换成 数据传输对象 List 集合
     *
     * @param accountRoleConnections 数据实体对象 {@link AccountRoleConnection} 的 List 集合
     * @return 数据传输对象 {@link AccountRoleConnectionDTO} 的 List 集合
     */
    List<AccountRoleConnectionDTO> entityToDTO(List<AccountRoleConnection> accountRoleConnections);
}
