package com.ranyk.vt.boot.example.system.log.api;

import com.ranyk.vt.boot.satoken.util.SecurityUtils;
import com.ranyk.vt.boot.example.system.log.converter.LoginLogConverter;
import com.ranyk.vt.boot.example.system.log.domain.po.LoginLogQueryPO;
import com.ranyk.vt.boot.example.system.log.domain.vo.LoginLogVO;
import com.ranyk.vt.boot.example.system.log.service.LoginLogService;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: LoginLogApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录日志查询接口, 面向管理员提供登录审计流水的分页查询
 * @date: 2026-11-16
 */
@RestController
@RequestMapping("/api/system/login-log")
@RequiredArgsConstructor
public class LoginLogApi {

    /**
     * 登录日志服务
     */
    private final LoginLogService loginLogService;
    /**
     * 登录日志入参转换器
     */
    private final LoginLogConverter loginLogConverter;

    /**
     * 分页查询登录审计日志。先校验管理员权限, 再按登录名、登录结果等条件过滤并分页返回,
     * 结果按登录时间倒序排列, 便于快速排查最近的登录情况。
     *
     * @param queryPO 分页与筛选参数, 包含页码、每页条数以及可选的登录名、是否成功条件
     * @return 分页结果, 包含当前页登录日志、总条数、页码与页大小
     */
    @PostMapping("/page")
    public MultiResult<LoginLogVO> page(@RequestBody LoginLogQueryPO queryPO) {
        SecurityUtils.requirePermission("system:login-log:list");
        return loginLogService.page(loginLogConverter.toQueryDTO(queryPO));
    }
}
