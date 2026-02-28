package com.ranyk.vt.boot.example.single.datasource.service.app.log;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.single.datasource.domain.dto.app.log.AppLogDTO;
import com.ranyk.vt.boot.example.single.datasource.domain.entity.app.log.AppLog;
import com.ranyk.vt.boot.example.single.datasource.mapper.app.log.AppLogMapper;
import com.ranyk.vt.boot.example.single.datasource.repository.app.log.AppLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: AppLogService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志业务逻辑处理
 * @date: 2026-02-26
 */
@Service
@SuppressWarnings("all")
public class AppLogService extends ServiceImpl<AppLogRepository, AppLog> {

    /**
     * 业务日志数据访问对象
     */
    private final AppLogRepository appLogRepository;
    /**
     * 业务日志对象转换 Mapper 对象
     */
    private final AppLogMapper appLogMapper;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入业务逻辑处理对象
     *
     * @param appLogRepository 业务日志数据访问对象
     * @param appLogMapper     业务日志对象转换 Mapper 对象
     */
    @Autowired
    public AppLogService(AppLogRepository appLogRepository, AppLogMapper appLogMapper) {
        this.appLogRepository = appLogRepository;
        this.appLogMapper = appLogMapper;
    }

    /**
     * 保存一条业务日志数据
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    public Boolean saveOne(AppLogDTO appLogDTO) {
        AppLog appLog = appLogMapper.dtoToEntity(appLogDTO);
        return this.save(appLog);
    }

    /**
     * 删除一条业务日志数据
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    public Boolean deleteOne(AppLogDTO appLogDTO) {
        AppLog appLog = appLogMapper.dtoToEntity(appLogDTO);
        return this.removeById(appLog);
    }

    /**
     * 更新一条业务日志数据
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 更新结果, true: 更新成功; false: 更新失败;
     */
    public Boolean updateOne(AppLogDTO appLogDTO) {
        AppLog appLog = appLogMapper.dtoToEntity(appLogDTO);
        return this.updateById(appLog);
    }

    /**
     * 获取业务日志列表
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 分页响应对象 {@link PageResponse}, 单个对象参见业务日志列表, {@link AppLogDTO} 列表
     */
    public PageResponse<AppLogDTO> queryList(AppLogDTO appLogDTO) {
        IPage<AppLog> page = PageUtils.buildPage(appLogDTO);
        LambdaQueryWrapper<AppLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(appLogDTO.getColumnOne()), AppLog::getColumnOne, appLogDTO.getColumnOne());
        queryWrapper.like(StrUtil.isNotBlank(appLogDTO.getColumnTwo()), AppLog::getColumnTwo, appLogDTO.getColumnTwo());
        IPage<AppLog> list = page(page, queryWrapper);
        return PageUtils.buildPageResponse(list, appLogMapper.entityListToDTOList(list.getRecords()));
    }
}
