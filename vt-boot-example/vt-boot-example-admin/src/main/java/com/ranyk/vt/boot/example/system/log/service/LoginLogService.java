package com.ranyk.vt.boot.example.system.log.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranyk.vt.boot.example.system.log.converter.LoginLogConverter;
import com.ranyk.vt.boot.example.system.log.domain.dto.LoginLogQueryDTO;
import com.ranyk.vt.boot.example.system.log.domain.entity.LoginLog;
import com.ranyk.vt.boot.example.system.log.domain.vo.LoginLogVO;
import com.ranyk.vt.boot.example.system.log.mapper.LoginLogMapper;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CLASS_NAME: LoginLogService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录日志服务
 * @date: 2026-11-16
 */
@Service
@RequiredArgsConstructor
public class LoginLogService {

    /**
     * 登录日志数据访问对象
     */
    private final LoginLogMapper loginLogMapper;
    /**
     * 登录日志实体转换器
     */
    private final LoginLogConverter loginLogConverter;

    /**
     * 按登录名和登录结果分页查询登录审计日志。查询条件为空时自动忽略,
     * 结果按登录时间倒序排列, 便于快速查看最近的登录流水。
     *
     * @param queryDTO 分页查询参数, 包含页码、每页条数及可选筛选条件
     * @return 包含当前页登录日志与总数、页码、页大小的分页结果
     */
    public MultiResult<LoginLogVO> page(LoginLogQueryDTO queryDTO) {
        Page<LoginLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<LoginLog> wrapper = Wrappers.<LoginLog>lambdaQuery()
                .like(StrUtil.isNotBlank(queryDTO.getUsername()), LoginLog::getUsername, queryDTO.getUsername())
                .eq(ObjectUtil.isNotNull(queryDTO.getSuccess()), LoginLog::getSuccess, queryDTO.getSuccess())
                .orderByDesc(LoginLog::getCreateTime);
        IPage<LoginLog> result = loginLogMapper.selectPage(page, wrapper);
        List<LoginLogVO> list = result.getRecords().stream().map(loginLogConverter::toVO).toList();
        return MultiResult.successMulti(list, result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }
}
