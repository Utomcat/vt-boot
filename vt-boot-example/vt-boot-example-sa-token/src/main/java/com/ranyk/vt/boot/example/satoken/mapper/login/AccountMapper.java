package com.ranyk.vt.boot.example.satoken.mapper.login;

import com.ranyk.vt.boot.example.satoken.domain.login.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.domain.login.entity.Account;
import com.ranyk.vt.boot.example.satoken.domain.login.po.AccountPO;
import com.ranyk.vt.boot.example.satoken.domain.login.vo.AccountVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
            @Mapping(target = "token", ignore = true)
    })
    AccountDTO loginRequestPOToDTO(AccountPO accountPO);

    /**
     * 数据传输对象转换成数据实体对象
     *
     * @param accountDTO 登录信息数据传输对象 {@link AccountDTO}
     * @return 账户信息数据实体对象 {@link Account}
     */
    @Mappings({
            @Mapping(source = "userName", target = "userName"),
            @Mapping(source = "password", target = "password"),
    })
    Account dtoToEntity(AccountDTO accountDTO);

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
}
