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
public class AppLogService extends ServiceImpl<AppLogRepository, AppLog> {

    private final AppLogRepository appLogRepository;

    @Autowired
    public AppLogService(AppLogRepository appLogRepository) {
        this.appLogRepository = appLogRepository;
    }

    public List<AppLogDTO> queryList() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }

    public Boolean saveOne(AppLogDTO appLogDTO) {
        AppLog appLog = BeanUtil.copyProperties(appLogDTO, AppLog.class);
        return this.save(appLog);
    }
}
