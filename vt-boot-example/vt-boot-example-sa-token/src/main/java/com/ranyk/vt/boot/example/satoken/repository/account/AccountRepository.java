package com.ranyk.vt.boot.example.satoken.repository.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.account.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: LoginAccountRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录账户数据库操作接口
 * @date: 2026-03-01
 */
@Mapper
@Component
public interface AccountRepository extends BaseMapper<Account> {

    /**
     * 通过账户ID查询账户信息 - 此 SQL 执行,不进行全局的逻辑删除数据的过滤
     *
     * @param id 账户ID
     * @return 账户信息数据实体对象 {@link Account}
     */
    Account selectOneAccountById(@Param("id") String id);

    /**
     * 更新账户信息 - 此 SQL 执行,不进行全局的逻辑删除数据的过滤
     *
     * @param account 账户信息数据实体对象 {@link Account}
     * @return 更新结果,true-更新成功,false-更新失败
     */
    Boolean updateOneAccountById(@Param("account") Account account);
}
