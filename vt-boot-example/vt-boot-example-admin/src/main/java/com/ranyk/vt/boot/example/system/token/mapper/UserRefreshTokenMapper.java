package com.ranyk.vt.boot.example.system.token.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.token.domain.entity.UserRefreshToken;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: UserRefreshTokenMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户刷新令牌 Mapper
 * @date: 2026-11-16
 */
@Mapper
public interface UserRefreshTokenMapper extends BaseMapper<UserRefreshToken> {
}
