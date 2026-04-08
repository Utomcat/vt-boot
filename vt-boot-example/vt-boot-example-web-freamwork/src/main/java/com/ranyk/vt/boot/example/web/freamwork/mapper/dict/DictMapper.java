package com.ranyk.vt.boot.example.web.freamwork.mapper.dict;

import com.ranyk.vt.boot.example.web.freamwork.domain.dict.dto.DictDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.dto.DictTypeDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.entity.Dict;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.entity.DictType;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.po.*;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo.DictTypeVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo.DictVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo.QueryDictVO;
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
     * 将 SaveDictTypePO 转换为 DictTypeDTO
     *
     * @param saveDictTypePO 字典类型保存PO对象 {@link SaveDictTypePO}
     * @return 字典类型DTO对象 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true ),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    DictTypeDTO saveDictTypePOToDTO(SaveDictTypePO saveDictTypePO);

    /**
     * 将 SaveDictPO 转换为 DictDTO
     *
     * @param saveDictPO 字典保存PO对象 {@link SaveDictPO}
     * @return 字典DTO对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "dictTypeCode", ignore = true),
    })
    DictDTO saveDictPOToDTO(SaveDictPO saveDictPO);

    /**
     * 将 DeleteDictTypePO 转换为 DictTypeDTO
     *
     * @param deleteDictTypePO 字典类型删除PO对象 {@link DeleteDictTypePO}
     * @return 字典类型DTO对象 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "remark", ignore = true),
    })
    DictTypeDTO deleteDictTypePOToDTO(DeleteDictTypePO deleteDictTypePO);

    /**
     * 将 DeleteDictPO 转换为 DictDTO
     *
     * @param deleteDictPO 字典删除PO对象 {@link DeleteDictPO}
     * @return 字典DTO对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "dictTypeId", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "value", ignore = true ),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "dictTypeCode", ignore = true),
    })
    DictDTO deleteDictPOToDTO(DeleteDictPO deleteDictPO);

    /**
     * 将 UpdateDictTypePO 转换为 DictTypeDTO
     *
     * @param updateDictTypePO 字典类型更新PO对象 {@link UpdateDictTypePO}
     * @return 字典类型DTO对象 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
    })
    DictTypeDTO updateDictTypePOToDTO(UpdateDictTypePO updateDictTypePO);

    /**
     * 将 UpdateDictPO 转换为 DictDTO
     *
     * @param updateDictPO 字典更新PO对象 {@link UpdateDictPO}
     * @return 字典DTO对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "dictTypeCode", ignore = true),
    })
    DictDTO updateDictPOToDTO(UpdateDictPO updateDictPO);

    /**
     * 将 QueryDictTypePO 转换为 DictTypeDTO
     *
     * @param queryDictTypePO 字典类型查询PO对象 {@link QueryDictTypePO}
     * @return 字典类型DTO对象 {@link DictTypeDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    DictTypeDTO queryDictTypePOToDTO(QueryDictTypePO queryDictTypePO);

    /**
     * 将 QueryDictPO 转换为 DictDTO
     *
     * @param queryDictPO 字典查询PO对象 {@link QueryDictPO}
     * @return 字典DTO对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "dictTypeCode", ignore = true),
    })
    DictDTO queryDictPOToDTO(QueryDictPO queryDictPO);

    /**
     * 将 QueryDictByTypeCodePO 转换为 DictDTO
     *
     * @param queryDictByTypeCodePO 字典查询PO对象 {@link QueryDictByTypeCodePO}
     * @return 字典DTO对象 {@link DictDTO}
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name", ignore = true),
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "value", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "tenantId", ignore = true),
            @Mapping(target = "remark", ignore = true),
            @Mapping(target = "createBy", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateBy", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "dictTypeCode", source = "code"),
            @Mapping(target = "ids", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true),
            @Mapping(target = "dictTypeId", ignore = true),
    })
    DictDTO queryDictByTypeCodePOToDictDTO(QueryDictByTypeCodePO queryDictByTypeCodePO);

    /**
     * 将 DictDTO 转换为 Dict 实体类
     *
     * @param dictDTO 字典 DTO 对象
     * @return 字典实体类对象 {@link Dict}
     */
    Dict dictDTOToDict(DictDTO dictDTO);

    /**
     * 将 Dict 实体类转换为 DictDTO
     *
     * @param dict 字典实体类对象 {@link Dict}
     * @return 字典DTO对象 {@link DictDTO}
     */
    @Mappings({

            @Mapping(target = "currentPage",ignore = true),
            @Mapping(target = "pageSize",ignore = true),
            @Mapping(target = "ids",ignore = true),
            @Mapping(target = "dictTypeCode", ignore = true),
    })
    DictDTO dictToDictDTO(Dict dict);

    /**
     * 将 Dict 列表转换为 DictDTO 列表
     *
     * @param dictList 字典列表
     * @return 字典DTO列表 {@link DictDTO}
     */
    List<DictDTO> dictListToDictDTOList(List<Dict> dictList);

    /**
     * 将 DictTypeDTO 转换为 DictType 实体类
     *
     * @param dictTypeDTO 字典类型 DTO 对象
     * @return 字典类型实体类对象 {@link DictType}
     */
    DictType dictTypeDTOToDictType(DictTypeDTO dictTypeDTO);

    /**
     * 将 DictType 实体类转换为 DictTypeDTO
     *
     * @param dictType 字典类型实体类对象 {@link DictType}
     * @return 字典类型DTO对象 {@link DictTypeDTO}
     */
    @Mappings({

            @Mapping(target = "currentPage",ignore = true),
            @Mapping(target = "pageSize",ignore = true),
            @Mapping(target = "ids",ignore = true)
    })
    DictTypeDTO dictTypeToDictTypeDTO(DictType dictType);

    /**
     * 将 DictType 列表转换为 DictTypeDTO 列表
     *
     * @param dictTypeList 字典类型列表
     * @return 字典类型DTO列表 {@link DictTypeDTO}
     */
    List<DictTypeDTO> dictTypeListToDictTypeDTOList(List<DictType> dictTypeList);

    /**
     * 将 DictTypeDTO 列表转换为 DictTypeVO 列表
     *
     * @param dictTypeDTOList 字典类型DTO列表 {@link DictTypeDTO}
     * @return 字典类型VO列表 {@link DictTypeVO}
     */
    List<DictTypeVO> dictTypeDTOListToVOList(List<DictTypeDTO> dictTypeDTOList);

    /**
     * 将 DictDTO 列表转换为 DictVO 列表
     *
     * @param dictDTOList 字典DTO列表 {@link DictDTO}
     * @return 字典VO列表 {@link DictVO}
     */
    List<DictVO> dictDTOListToVOList(List<DictDTO> dictDTOList);

    /**
     * 将 DictDTO 转换为 QueryDictVO
     *
     * @param dictDTO 字典DTO对象 {@link DictDTO}
     * @return 字典VO对象 {@link QueryDictVO}
     */
    QueryDictVO dictDTOToQueryDictVO(DictDTO dictDTO);

    /**
     * 将 DictDTO 列表转换为 QueryDictVO 列表
     *
     * @param dictDTOList 字典DTO列表 {@link DictDTO}
     * @return 字典VO列表 {@link QueryDictVO}
     */
    List<QueryDictVO> dictDTOToQueryDictVO(List<DictDTO> dictDTOList);
}
