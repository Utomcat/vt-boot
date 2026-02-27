package com.ranyk.vt.boot.example.single.datasource.mapper;

import com.ranyk.vt.boot.example.single.datasource.domain.dto.AppLogDTO;
import com.ranyk.vt.boot.example.single.datasource.domain.entity.AppLog;
import com.ranyk.vt.boot.example.single.datasource.domain.po.AppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.vo.AppLogVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * CLASS_NAME: AppLogMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志对象转换 Mapper 接口
 * @date: 2026-02-27
 */
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface AppLogMapper {

    /**
     * {@link AppLogPO} 对象转 {@link AppLogDTO} 对象
     *
     * @param appLogPO 业务日志数据对象 {@link AppLogPO}
     * @return 业务日志数据传输对象 {@link AppLogDTO}
     */
    AppLogDTO poToDTO(AppLogPO appLogPO);

    /**
     * {@link AppLogDTO} 对象转 {@link AppLogVO} 对象
     *
     * @param appLogDTO 业务日志数据传输对象 {@link AppLogDTO}
     * @return 业务日志数据视图对象 {@link AppLogVO}
     */
    AppLogVO dtoToVO(AppLogDTO appLogDTO);

    /**
     * {@link AppLogDTO} 对象转 {@link AppLog} 持久化对象
     *
     * @param appLogDTO 业务日志数据传输对象 {@link AppLogDTO}
     * @return 业务日志数据持久化对象 {@link AppLog}
     */
    AppLog dtoToEntity(AppLogDTO appLogDTO);

    /**
     * {@link AppLogDTO} List 集合转 {@link AppLogVO} List 集合
     *
     * @param appLogDTOList 业务日志数据传输对象 {@link AppLogDTO} List 集合
     * @return 业务日志数据视图对象 {@link AppLogVO} List 集合
     */
    List<AppLogVO> dtoListToVOList(List<AppLogDTO> appLogDTOList);

    /**
     * {@link AppLogPO} List 集合转 {@link AppLogDTO} List 集合
     *
     * @param appLogPOList 业务日志数据持久化对象 {@link AppLogPO} List 集合
     * @return 业务日志数据传输对象 {@link AppLogDTO} List 集合
     */
    List<AppLogDTO> poListToDTOList(List<AppLogPO> appLogPOList);

    /**
     * {@link AppLog} List 集合转 {@link AppLogDTO} List 集合
     *
     * @param appLogList 业务日志数据持久化对象 {@link AppLog} List 集合
     * @return 业务日志数据传输对象 {@link AppLogDTO} List 集合
     */
    List<AppLogDTO> entityListToDTOList(List<AppLog> appLogList);
}
