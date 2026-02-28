package com.ranyk.vt.boot.example.single.datasource.service.app.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.single.datasource.domain.entity.app.service.AppServiceTable;
import com.ranyk.vt.boot.example.single.datasource.repository.app.service.AppServiceTableRepository;
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

    /**
     * 业务测试表数据访问对象
     */
    private final AppServiceTableRepository appServiceTableRepository;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入业务逻辑处理对象
     *
     * @param appServiceTableRepository 业务测试表数据访问对象
     */
    @Autowired
    public AppServiceTableService(AppServiceTableRepository appServiceTableRepository) {
        this.appServiceTableRepository = appServiceTableRepository;
    }
}
