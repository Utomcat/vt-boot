package com.ranyk.vt.boot.example.auth.converter;

import com.ranyk.vt.boot.example.auth.domain.dto.LoginDTO;
import com.ranyk.vt.boot.example.auth.domain.dto.RefreshTokenDTO;
import com.ranyk.vt.boot.example.auth.domain.po.LoginPO;
import com.ranyk.vt.boot.example.auth.domain.po.RefreshTokenPO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: AuthConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 认证接口入参与业务 DTO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-16
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthConverter {

    /**
     * 登录接口入参转登录业务 DTO(API 层入参 -> Service 层参数)
     *
     * @param loginPO 登录接口入参
     * @return 登录请求参数 {@link LoginDTO}
     */
    LoginDTO toLoginDTO(LoginPO loginPO);

    /**
     * 刷新令牌接口入参转刷新令牌业务 DTO(API 层入参 -> Service 层参数)
     *
     * @param refreshTokenPO 刷新令牌接口入参
     * @return 刷新令牌请求参数 {@link RefreshTokenDTO}
     */
    RefreshTokenDTO toRefreshTokenDTO(RefreshTokenPO refreshTokenPO);
}
