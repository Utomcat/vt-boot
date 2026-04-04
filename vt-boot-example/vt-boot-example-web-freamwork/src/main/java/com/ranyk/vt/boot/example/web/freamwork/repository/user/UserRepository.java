package com.ranyk.vt.boot.example.web.freamwork.repository.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ranyk.vt.boot.example.web.freamwork.domain.account.dto.AccountUserConnectionDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: UserRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息数据库操作接口
 * @date: 2026-03-17
 */
@Mapper
@Component
public interface UserRepository extends BaseMapper<User> {

    /**
     * 根据用户 ID 查询用户信息 - 此 SQL 执行,不进行全局的逻辑删除数据的过滤
     *
     * @param id 用户 ID
     * @return 用户信息 数据实体对象 {@link User}
     */
    User selectOneUserById(@Param("id") String id);

    /**
     * 根据用户名称和账户名称模糊查询账户用户关联关系信息
     *
     * @param userName 用户名称
     * @param accountName 账户名称
     * @return 账户用户关联关系信息 分页查询对象 {@link IPage}, {@link AccountUserConnectionDTO}
     */
    IPage<AccountUserConnectionDTO> selectByUserNameLikeAndAccountNameLike(IPage<?> page, @Param("userName") String userName, @Param("accountName") String accountName);
}
