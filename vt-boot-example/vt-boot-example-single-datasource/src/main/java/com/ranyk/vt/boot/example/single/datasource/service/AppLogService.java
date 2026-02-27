package com.ranyk.vt.boot.example.single.datasource.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.single.datasource.domain.dto.AppLogDTO;
import com.ranyk.vt.boot.example.single.datasource.domain.entity.AppLog;
import com.ranyk.vt.boot.example.single.datasource.repository.AppLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 构造方法 - 向 Spring IOC 容器中自动注入业务逻辑处理对象
     *
     * @param appLogRepository 业务日志数据访问对象
     */
    @Autowired
    public AppLogService(AppLogRepository appLogRepository) {
        this.appLogRepository = appLogRepository;
    }

    /**
     * 获取业务日志列表
     *
     * @return 业务日志列表, {@link AppLogDTO} 列表
     */
    public List<AppLogDTO> queryList() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }

    /**
     * 保存一条业务日志数据
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    public Boolean saveOne(AppLogDTO appLogDTO) {
        AppLog appLog = BeanUtil.copyProperties(appLogDTO, AppLog.class);
        return this.save(appLog);
    }
}
