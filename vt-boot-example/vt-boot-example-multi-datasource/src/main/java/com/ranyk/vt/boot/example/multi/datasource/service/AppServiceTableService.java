package com.ranyk.vt.boot.example.multi.datasource.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.multi.datasource.domain.entity.AppServiceTable;
import com.ranyk.vt.boot.example.multi.datasource.repository.AppServiceTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: AppServiceTableService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务测试表业务逻辑处理
 * @date: 2026-02-26
 */
@Service
public class AppServiceTableService extends ServiceImpl<AppServiceTableRepository, AppServiceTable> {

    private final AppServiceTableRepository appServiceTableRepository;

    @Autowired
    public AppServiceTableService(AppServiceTableRepository appServiceTableRepository) {
        this.appServiceTableRepository = appServiceTableRepository;
    }
}
