package com.ranyk.vt.boot.example.system.dict.converter;

import com.ranyk.vt.boot.example.system.dict.domain.dto.DictDataFormDTO;
import com.ranyk.vt.boot.example.system.dict.domain.dto.DictDataQueryDTO;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictData;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictDataFormPO;
import com.ranyk.vt.boot.example.system.dict.domain.po.DictDataQueryPO;
import com.ranyk.vt.boot.example.system.dict.domain.vo.DictDataVO;
import com.ranyk.vt.boot.example.system.dict.reader.DictItem;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * CLASS_NAME: SysDictDataConverter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据实体与 DTO/VO 之间的转换器, 基于 MapStruct 在编译期生成实现
 * @date: 2026-11-20
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDictDataConverter {

    /**
     * 接口入参转业务查询 DTO
     *
     * @param queryPO 字典数据分页查询接口入参
     * @return 字典数据分页查询参数 {@link DictDataQueryDTO}
     */
    DictDataQueryDTO toQueryDTO(DictDataQueryPO queryPO);

    /**
     * 接口入参转业务表单 DTO
     *
     * @param formPO 字典数据新增/修改接口入参
     * @return 字典数据表单参数 {@link DictDataFormDTO}
     */
    DictDataFormDTO toFormDTO(DictDataFormPO formPO);

    /**
     * 字典数据实体转视图对象
     *
     * @param dictData 字典数据实体
     * @return 字典数据视图对象 {@link DictDataVO}
     */
    DictDataVO toVO(SysDictData dictData);

    /**
     * 读取侧字典项转视图对象, 用于对外提供字典读取接口
     *
     * @param item 读取侧字典项
     * @return 字典数据视图对象 {@link DictDataVO}
     */
    @Mapping(target = "dictLabel", source = "label")
    @Mapping(target = "dictValue", source = "value")
    @Mapping(target = "dictSort", source = "sort")
    DictDataVO toVO(DictItem item);

    /**
     * 字典数据表单转实体(新增场景), 主键与审计字段由数据库自动填充
     *
     * @param formDTO 字典数据表单参数
     * @return 字典数据实体 {@link SysDictData}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SysDictData toEntity(DictDataFormDTO formDTO);

    /**
     * 将字典数据表单的非空字段合并到已有实体(修改场景); 审计字段不在此处覆盖
     *
     * @param formDTO  字典数据表单参数
     * @param dictData 待更新的字典数据实体
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    void updateEntity(DictDataFormDTO formDTO, @MappingTarget SysDictData dictData);
}
