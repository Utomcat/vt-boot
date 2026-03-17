package com.ranyk.vt.boot.example.web.freamwork.repository.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.entity.AccountRoleConnection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CLASS_NAME: AccountRoleConnectionRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库账户角色关联关系操作接口
 * @date: 2026-03-03
 */
@Mapper
@Component
public interface AccountRoleConnectionRepository extends BaseMapper<AccountRoleConnection> {

    /**
     * 通过账户ID删除账户角色关联关系数据
     *
     * @param accountId 账户ID
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    Boolean deleteByAccountId(@Param("accountId") String accountId);

    /**
     * 通过账户ID列表批量删除账户角色关联关系数据
     *
     * @param accountIds 账户ID列表
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    Boolean deleteByAccountIdIn(@Param("accountIds") List<String> accountIds);

    /**
     * 通过ID删除账户角色关联关系数据
     *
     * @param id 数据主键 ID
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    Boolean deleteByIdEq(@Param("id") String id);

    /**
     * 通过ID列表批量删除账户角色关联关系数据
     *
     * @param ids 数据主键 ID 列表
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    Boolean deleteByIdIn(@Param("ids") List<String> ids);
}
