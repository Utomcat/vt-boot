package com.ranyk.vt.boot.example.satoken.mapper.dict;

import com.ranyk.vt.boot.example.satoken.domain.dict.dto.DictDTO;
import com.ranyk.vt.boot.example.satoken.domain.dict.dto.DictTypeDTO;
import com.ranyk.vt.boot.example.satoken.domain.dict.entity.Dict;
import com.ranyk.vt.boot.example.satoken.domain.dict.entity.DictType;
import com.ranyk.vt.boot.example.satoken.domain.dict.po.*;
import com.ranyk.vt.boot.example.satoken.domain.dict.vo.DictTypeVO;
import com.ranyk.vt.boot.example.satoken.domain.dict.vo.DictVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * CLASS_NAME: DictMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据字典数据转换映射接口 Mapper
 * @date: 2026-03-05
 */
@Mapper(componentModel = "spring")
public interface DictMapper {

    /**
     * 新增字典类型操作 - 前端传入参数 PO 对象转换为 DTO 对象
     *
     * @param saveDictTypePO 保存字典类型 PO 对象 {@link SaveDictTypePO}
     * @return 返回字典类型 DTO 对象 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    DictTypeDTO saveDictTypePOToDTO(SaveDictTypePO saveDictTypePO);

    /**
     * 新增字典操作 - 前端传入参数 PO 对象转换为 DTO 对象
     *
     * @param saveDictPO 保存字典 PO 对象 {@link SaveDictPO}
     * @return 返回字典 DTO 对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    DictDTO saveDictPOToDTO(SaveDictPO saveDictPO);

    /**
     * 新增字典类型操作 - 前端传入参数 DTO 对象转换为 Entity 对象
     *
     * @param dictTypeDTO 字典类型 DTO 删除字典类型 DTO 删除字典类型 DTO 对象 {@link DictTypeDTO}
     * @return 返回字典类型 Entity 对象 {@link DictType}
     */
    DictType dictTypeDTOToEntity(DictTypeDTO dictTypeDTO);

    /**
     * 新增字典操作 - 前端传入参数 DTO 删除字典
     *
     * @param dictDTO 字典 DTO 删除字典 DTO 删除字典 DTO 对象 {@link DictDTO}
     * @return 删除字典 Entity 删除字典 Entity 对象 {@link Dict}
     */
    Dict dictDTOToEntity(DictDTO dictDTO);

    /**
     * 删除字典类型操作 - 前端传入参数 PO 删除字典类型
     *
     * @param deleteDictTypePO 删除字典类型 PO 删除字典类型 PO 对象 {@link DeleteDictTypePO}
     * @return 删除字典类型 DTO 删除字典类型 DTO 对象 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
    })
    DictTypeDTO deleteDictTypePOToDTO(DeleteDictTypePO deleteDictTypePO);

    /**
     * 删除字典操作 - 前端传入参数 PO 删除字典
     *
     * @param deleteDictPO 删除字典 PO 删除字典 PO 对象 {@link DeleteDictTypePO}
     * @return 删除字典 DTO 删除字典 DTO 对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "value", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "dictTypeId", ignore = true)
    })
    DictDTO deleteDictPOToDTO(DeleteDictPO deleteDictPO);

    /**
     * 修改字典类型操作 - 前端传入参数 PO 修改字典类型
     *
     * @param updateDictTypePO 修改字典类型 PO 修改字典类型 PO 对象 {@link UpdateDictTypePO}
     * @return 修改字典类型 DTO 修改字典类型 DTO 对象 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    DictTypeDTO updateDictTypePOToDTO(UpdateDictTypePO updateDictTypePO);

    /**
     * 修改字典操作 - 前端传入参数 PO 删除字典
     *
     * @param updateDictPO 修改字典 PO 修改字典 PO 对象 {@link UpdateDictPO}
     * @return 修改字典 DTO 修改字典 DTO 对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    DictDTO updateDictPOToDTO(UpdateDictPO updateDictPO);

    /**
     * 查询字典类型操作 - 前端传入参数 PO 查询字典类型
     *
     * @param queryDictTypePO 查询字典类型 PO 查询字典类型 PO 对象 {@link QueryDictTypePO}
     * @return 查询字典类型 DTO 查询字典类型 DTO 列表 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    DictTypeDTO queryDictTypePOToDTO(QueryDictTypePO queryDictTypePO);

    /**
     * 查询字典操作 - 前端传入参数 PO 查询字典
     *
     * @param queryDictPO 查询字典 PO 查询字典 PO 列表 {@link QueryDictPO}
     * @return 查询字典 DTO 查询字典 DTO 列表 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true)
    })
    DictDTO queryDictPOToDTO(QueryDictPO queryDictPO);

    /**
     * 字典类型实体列表转换成字典类型 DTO 列表
     *
     * @param dictTypeList 字典类型实体列表 {@link DictType}
     * @return 字典类型 DTO 列表 {@link DictTypeDTO}
     */
    List<DictTypeDTO> dictTypeEntityListToDTOList(List<DictType> dictTypeList);

    /**
     * 字典类型 DTO 列表转换成字典类型 VO 列表
     *
     * @param dictTypeList 字典类型 DTO 列表 {@link DictTypeDTO}
     * @return 字典类型 VO 列表 {@link DictTypeVO}
     */
    List<DictTypeVO> dictTypeDTOListToVOList(List<DictTypeDTO> dictTypeList);

    /**
     * 字典实体列表转换成字典 DTO 列表
     *
     * @param dictList 字典实体列表 {@link Dict}
     * @return 字典 DTO 列表 {@link DictDTO}
     */
    List<DictDTO> dictEntityListToDTOList(List<Dict> dictList);

    /**
     * 字典 DTO 列表转换成字典 VO 列表
     *
     * @param dictDTOList 字典 DTO 列表 {@link DictDTO}
     * @return 字典 VO 列表 {@link DictVO}
     */
    List<DictVO> dictDTOListToVOList(List<DictDTO> dictDTOList);
}
