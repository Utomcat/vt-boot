package com.ranyk.vt.boot.example.system.dict.converter;

import com.ranyk.vt.boot.example.system.dict.domain.dto.DictTypeFormDTO;
import com.ranyk.vt.boot.example.system.dict.domain.dto.DictTypeQueryDTO;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictType;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictTypeFormPO;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictTypeQueryPO;
import com.ranyk.vt.boot.example.system.dict.domain.vo.DictTypeVO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: SysDictTypeConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型实体与 DTO/VO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-20
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictTypeConverter {

    /**
     * 接口入参转业务查询 DTO
     *
     * @param queryPO 字典类型分页查询接口入参
     * @return 字典类型分页查询参数 {@link DictTypeQueryDTO}
     */
    DictTypeQueryDTO toQueryDTO(DictTypeQueryPO queryPO);

    /**
     * 接口入参转业务表单 DTO
     *
     * @param formPO 字典类型新增/修改接口入参
     * @return 字典类型表单参数 {@link DictTypeFormDTO}
     */
    DictTypeFormDTO toFormDTO(DictTypeFormPO formPO);

    /**
     * 字典类型实体转视图对象
     *
     * @param dictType 字典类型实体
     * @return 字典类型视图对象 {@link DictTypeVO}
     */
    DictTypeVO toVO(SysDictType dictType);

    /**
     * 字典类型表单转实体(新增场景), 主键与审计字段由数据库自动填充
     *
     * @param formDTO 字典类型表单参数
     * @return 字典类型实体 {@link SysDictType}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SysDictType toEntity(DictTypeFormDTO formDTO);

    /**
     * 将字典类型表单的非空字段合并到已有实体(修改场景); 审计字段不在此处覆盖
     *
     * @param formDTO  字典类型表单参数
     * @param dictType 待更新的字典类型实体
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    void updateEntity(DictTypeFormDTO formDTO, @MappingTarget SysDictType dictType);
}
