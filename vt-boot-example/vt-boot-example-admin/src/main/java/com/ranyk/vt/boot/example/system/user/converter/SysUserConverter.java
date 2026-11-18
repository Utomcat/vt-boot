package com.ranyk.vt.boot.example.system.user.converter;

import com.ranyk.vt.boot.example.auth.domain.vo.UserInfoVO;
import com.ranyk.vt.boot.example.system.user.domain.dto.UserFormDTO;
import com.ranyk.vt.boot.example.system.user.domain.dto.UserQueryDTO;
import com.ranyk.vt.boot.example.system.user.domain.entity.SysUser;
import com.ranyk.vt.boot.example.system.user.domain.po.UserFormPO;
import com.ranyk.vt.boot.example.system.user.domain.po.UserQueryPO;
import com.ranyk.vt.boot.example.system.user.domain.vo.UserVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: SysUserConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户实体与 DTO/VO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-16
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysUserConverter {

    /**
     * 接口入参转业务查询 DTO(API 层入参 -> Service 层参数)
     *
     * @param queryPO 用户分页查询接口入参
     * @return 用户分页查询参数 {@link UserQueryDTO}
     */
    UserQueryDTO toQueryDTO(UserQueryPO queryPO);

    /**
     * 接口入参转业务表单 DTO(API 层入参 -> Service 层参数)
     *
     * @param formPO 用户新增/修改接口入参
     * @return 用户表单参数 {@link UserFormDTO}
     */
    UserFormDTO toFormDTO(UserFormPO formPO);

    /**
     * 用户实体转用户视图对象
     *
     * @param user 用户实体
     * @return 用户视图对象 {@link UserVO}
     */
    UserVO toVO(SysUser user);

    /**
     * 用户实体转当前登录用户信息视图对象, userId 取自实体主键 id
     *
     * @param user 用户实体
     * @return 当前登录用户信息 {@link UserInfoVO}
     */
    @Mapping(target = "userId", source = "id")
    UserInfoVO toUserInfo(SysUser user);

    /**
     * 用户表单转用户实体(新增场景), 主键与审计字段由数据库自动填充
     *
     * @param formDTO 用户表单参数
     * @return 用户实体 {@link SysUser}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "loginFailCount", ignore = true)
    @Mapping(target = "lockTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    SysUser toEntity(UserFormDTO formDTO);

    /**
     * 将用户表单的非空字段合并到已有用户实体(修改场景); 密码、登录名与审计字段不在此处覆盖
     *
     * @param formDTO 用户表单参数
     * @param user    待更新的用户实体
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "loginFailCount", ignore = true)
    @Mapping(target = "lockTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    @Mapping(target = "lastLoginIp", ignore = true)
    void updateEntity(UserFormDTO formDTO, @MappingTarget SysUser user);
}
