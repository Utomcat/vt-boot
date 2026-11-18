package com.ranyk.vt.boot.example.system.log.converter;

import com.ranyk.vt.boot.example.system.log.domain.dto.LoginLogQueryDTO;
import com.ranyk.vt.boot.example.system.log.domain.entity.LoginLog;
import com.ranyk.vt.boot.example.system.log.domain.po.LoginLogQueryPO;
import com.ranyk.vt.boot.example.system.log.domain.vo.LoginLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: LoginLogConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录日志实体与 VO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-16
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LoginLogConverter {

    /**
     * 接口入参转业务查询 DTO(API 层入参 -> Service 层参数)
     *
     * @param queryPO 登录日志分页查询接口入参
     * @return 登录日志分页查询参数 {@link LoginLogQueryDTO}
     */
    LoginLogQueryDTO toQueryDTO(LoginLogQueryPO queryPO);

    /**
     * 登录日志实体转登录日志视图对象
     *
     * @param loginLog 登录日志实体
     * @return 登录日志视图对象 {@link LoginLogVO}
     */
    LoginLogVO toVO(LoginLog loginLog);
}
