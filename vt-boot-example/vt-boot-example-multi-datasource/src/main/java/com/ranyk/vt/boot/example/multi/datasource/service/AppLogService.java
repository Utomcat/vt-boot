package com.ranyk.vt.boot.example.multi.datasource.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.example.multi.datasource.domain.dto.AppLogDTO;
import com.ranyk.vt.boot.example.multi.datasource.domain.entity.AppLog;
import com.ranyk.vt.boot.example.multi.datasource.repository.AppLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CLASS_NAME: AppLogService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志业务逻辑处理
 * @date: 2026-02-26
 */
@Slf4j
@Service
public class AppLogService extends ServiceImpl<AppLogRepository, AppLog> {

    /**
     * 业务日志数据库操作接口对象
     */
    private final AppLogRepository appLogRepository;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入业务日志数据库操作接口对象
     *
     * @param appLogRepository 业务日志数据库操作接口对象
     */
    @Autowired
    public AppLogService(AppLogRepository appLogRepository) {
        this.appLogRepository = appLogRepository;
    }

    /**
     * 保存一条业务日志数据 - 从数据源一中保存数据, 使用 MyBatis Plus 的 saveOrUpdate 方法
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    @DS("master")
    @DSTransactional(rollbackFor = Exception.class)
    public Boolean saveOneDataDbOne(AppLogDTO appLogDTO) {
        AppLog appLog = BeanUtil.copyProperties(appLogDTO, AppLog.class);
        return saveOrUpdate(appLog);
    }

    /**
     * 批量保存业务日志数据 - 从数据源二中保存数据, 使用自定义的批量保存方法
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    @DS("secondary")
    @DSTransactional(rollbackFor = Exception.class)
    public Boolean saveOneDataDbTwo(AppLogDTO appLogDTO) {
        AppLog appLog = BeanUtil.copyProperties(appLogDTO, AppLog.class);
        return appLogRepository.insertOneData(appLog);
    }

    /**
     * 批量保存业务日志数据 - 从数据源三中保存数据, 使用自定义的批量保存方法
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 批量保存结果, true: 批量保存成功; false: 批量保存失败;
     */
    @DS("third")
    @DSTransactional(rollbackFor = Exception.class)
    public Boolean saveOneDataDbThree(AppLogDTO appLogDTO) {
        AppLog appLog = BeanUtil.copyProperties(appLogDTO, AppLog.class);
        return appLogRepository.insertOneData(appLog);
    }

    /**
     * 保存一条业务日志数据异常测试, 测试 @DSTransactional 注解的回滚功能
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 批量保存结果, true: 批量保存成功; false: 批量保存失败;
     */
    @DS("master")
    @DSTransactional(rollbackFor = Exception.class)
    public Boolean saveOneDataExceptionOneDbOne(AppLogDTO appLogDTO) {
        AppLog appLog = BeanUtil.copyProperties(appLogDTO, AppLog.class);
        boolean saveResult = saveOrUpdate(appLog);
        int quotient = 1 / 0;
        log.info("对数据源一进行异常测试 quotient: {}", quotient);
        return saveResult;
    }

    /**
     * 保存一条业务日志数据异常测试, 测试 @Transactional 注解的回滚功能
     *
     * @param appLogDTO 业务日志数据对象 {@link AppLogDTO}
     * @return 批量保存结果, true: 批量保存成功; false: 批量保存失败;
     */
    @DS("secondary")
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOneDataExceptionOneDbTwo(AppLogDTO appLogDTO) {
        AppLog appLog = BeanUtil.copyProperties(appLogDTO, AppLog.class);
        boolean saveResult = saveOrUpdate(appLog);
        int quotient = 1 / 0;
        log.info("对数据源二进行异常测试 quotient: {}", quotient);
        return saveResult;
    }

    /**
     * 获取业务日志列表 - 从数据源一中查询数据
     *
     * @return 业务日志列表, {@link AppLogDTO} 对象 List 集合
     */
    @DS("master")
    public List<AppLogDTO> queryListDbOne() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }

    /**
     * 获取业务日志列表 - 从数据源二中查询数据
     *
     * @return 业务日志列表, {@link AppLogDTO} 对象 List 集合
     */
    @DS("secondary")
    public List<AppLogDTO> queryListDbTwo() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }

    /**
     * 获取业务日志列表 - 从数据源三中查询数据
     *
     * @return 业务日志列表, {@link AppLogDTO} 对象 List 集合
     */
    @DS("third")
    public List<AppLogDTO> queryListDbThird() {
        List<AppLog> list = list();
        return BeanUtil.copyToList(list, AppLogDTO.class);
    }
}
