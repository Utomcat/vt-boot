package com.ranyk.vt.boot.example.web.freamwork.repository.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountUserConnection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: AccountUserRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 账户用户数据库操作接口
 * @date: 2026-03-17
 */
@Mapper
@Component
public interface AccountUserRepository extends BaseMapper<AccountUserConnection> {

    /**
     * 根据账户ID 和 用户ID 删除一条账户用户关系信息, 物理删除数据
     *
     * @param accountId 账户ID
     * @param userId    用户ID
     * @return 删除结果
     */
    Boolean deleteOneByAccountIdEqAndUserIdEq(@Param("accountId") String accountId, @Param("userId") String userId);

    /**
     * 根据ID 删除一条账户用户关系信息, 物理删除数据
     *
     * @param id 账户用户关联关系数据ID
     * @return 删除结果, true: 删除成功, false: 删除失败;
     */
    Boolean deleteOneById(@Param("id") String id);
}
