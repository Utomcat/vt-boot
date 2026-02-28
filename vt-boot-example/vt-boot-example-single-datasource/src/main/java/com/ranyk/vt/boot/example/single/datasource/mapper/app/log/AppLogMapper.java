package com.ranyk.vt.boot.example.single.datasource.mapper.app.log;

import com.ranyk.vt.boot.example.single.datasource.domain.dto.app.log.AppLogDTO;
import com.ranyk.vt.boot.example.single.datasource.domain.entity.app.log.AppLog;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.DeleteAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.QueryAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.SaveAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.UpdateAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.vo.app.log.QueryAppLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * CLASS_NAME: AppLogMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志对象转换 Mapper 接口
 * @date: 2026-02-27
 */
@Mapper(componentModel = "spring")
public interface AppLogMapper {

    /**
     * {@link SaveAppLogPO} 对象转 {@link AppLogDTO} 对象
     *
     * @param appLogPO 业务日志数据对象 {@link SaveAppLogPO}
     * @return 业务日志数据传输对象 {@link AppLogDTO}
     */
    @Mappings(value = {
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true)
    })
    AppLogDTO savePOToDTO(SaveAppLogPO appLogPO);

    /**
     * {@link DeleteAppLogPO} 删除业务日志数据对象转 {@link AppLogDTO} 业务日志数据传输对象
     *
     * @param deleteAppLogPO 业务日志数据删除对象 {@link DeleteAppLogPO}
     * @return 业务日志数据传输对象 {@link AppLogDTO}
     */
    @Mappings(value = {
            @Mapping(target = "columnOne", ignore = true),
            @Mapping(target = "columnTwo", ignore = true),
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true)
    })
    AppLogDTO deletePOToDTO(DeleteAppLogPO deleteAppLogPO);

    /**
     * {@link UpdateAppLogPO} 更新业务日志数据对象转 {@link AppLogDTO} 对象
     *
     * @param updateAppLogPO 业务日志数据更新对象 {@link UpdateAppLogPO}
     * @return 业务日志数据传输对象 {@link AppLogDTO}
     */
    @Mappings(value = {
            @Mapping(target = "currentPage", ignore = true),
            @Mapping(target = "pageSize", ignore = true)
    })
    AppLogDTO updatePOToDTO(UpdateAppLogPO updateAppLogPO);

    /**
     * {@link QueryAppLogPO} 列表查询业务日志数据对象转 {@link AppLogDTO} 业务日志数据传输对象
     *
     * @param queryAppLogPO 业务日志数据查询对象 {@link QueryAppLogPO}
     * @return 业务日志数据传输对象 {@link AppLogDTO}
     */
    AppLogDTO queryPOToDTO(QueryAppLogPO queryAppLogPO);

    /**
     * {@link AppLogDTO} 对象转 {@link QueryAppLogVO} 对象
     *
     * @param appLogDTO 业务日志数据传输对象 {@link AppLogDTO}
     * @return 业务日志数据视图对象 {@link QueryAppLogVO}
     */
    QueryAppLogVO dtoToQueryVO(AppLogDTO appLogDTO);

    /**
     * {@link AppLogDTO} 对象转 {@link AppLog} 持久化对象
     *
     * @param appLogDTO 业务日志数据传输对象 {@link AppLogDTO}
     * @return 业务日志数据持久化对象 {@link AppLog}
     */
    AppLog dtoToEntity(AppLogDTO appLogDTO);

    /**
     * {@link AppLogDTO} List 集合转 {@link QueryAppLogVO} List 集合
     *
     * @param appLogDTOList 业务日志数据传输对象 {@link AppLogDTO} List 集合
     * @return 业务日志数据视图对象 {@link QueryAppLogVO} List 集合
     */
    List<QueryAppLogVO> dtoListToQueryVOList(List<AppLogDTO> appLogDTOList);

    /**
     * {@link SaveAppLogPO} List 集合转 {@link AppLogDTO} List 集合
     *
     * @param appLogPOList 业务日志数据持久化对象 {@link SaveAppLogPO} List 集合
     * @return 业务日志数据传输对象 {@link AppLogDTO} List 集合
     */
    List<AppLogDTO> savePOListToDTOList(List<SaveAppLogPO> appLogPOList);

    /**
     * {@link AppLog} List 集合转 {@link AppLogDTO} List 集合
     *
     * @param appLogList 业务日志数据持久化对象 {@link AppLog} List 集合
     * @return 业务日志数据传输对象 {@link AppLogDTO} List 集合
     */
    List<AppLogDTO> entityListToDTOList(List<AppLog> appLogList);
}
