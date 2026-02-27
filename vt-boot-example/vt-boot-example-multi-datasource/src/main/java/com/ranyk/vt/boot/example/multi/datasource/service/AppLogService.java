package com.ranyk.vt.boot.example.multi.datasource.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.multi.datasource.domain.dto.AppLogDTO;
import com.ranyk.vt.boot.example.multi.datasource.domain.entity.AppLog;
import com.ranyk.vt.boot.example.multi.datasource.repository.AppLogRepository;
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

    @DS("master")
    public List<AppLogDTO> queryListDbOne() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }

    @DS("secondary")
    public List<AppLogDTO> queryListDbTwo() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }

    @DS("third")
    public List<AppLogDTO> queryListDbThird() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }

}
