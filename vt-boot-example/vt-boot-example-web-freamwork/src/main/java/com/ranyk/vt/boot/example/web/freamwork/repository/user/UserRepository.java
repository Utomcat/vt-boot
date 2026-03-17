package com.ranyk.vt.boot.example.web.freamwork.repository.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.web.freamwork.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
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
}
