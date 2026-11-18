package com.ranyk.vt.boot.task.admin.controller;

import com.ranyk.vt.boot.task.admin.domain.dto.VtJobLogQueryDTO;
import com.ranyk.vt.boot.task.admin.domain.vo.VtJobLogVO;
import com.ranyk.vt.boot.task.admin.service.VtJobLogService;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * CLASS_NAME: VtJobLogController.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志接口, 提供日志分页、详情、状态统计与按保留天数清理
 * @date: 2026-11-18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task/log")
public class VtJobLogController {

    /**
     * 任务执行日志业务对象
     */
    private final VtJobLogService jobLogService;

    /**
     * 分页查询执行日志。可按任务名称、状态与触发时间范围筛选, 按主键倒序返回。
     *
     * @param queryDTO 分页查询参数
     * @return 包含当前页日志列表与总数的分页结果
     */
    @GetMapping("/list")
    public MultiResult<VtJobLogVO> list(VtJobLogQueryDTO queryDTO) {
        return jobLogService.page(queryDTO);
    }

    /**
     * 查询执行日志详情。
     *
     * @param id 日志主键
     * @return 日志详细信息
     */
    @GetMapping("/detail/{id}")
    public Result<VtJobLogVO> detail(@PathVariable Long id) {
        return Result.success(jobLogService.detail(id));
    }

    /**
     * 统计各状态日志数量。
     *
     * @return 状态 -> 数量
     */
    @GetMapping("/summary")
    public Result<Map<String, Long>> summary() {
        return Result.success(jobLogService.summary());
    }

    /**
     * 按保留天数清理历史日志。
     *
     * @param days 保留天数, 不传时默认保留 30 天
     * @return 清理条数
     */
    @DeleteMapping("/clean")
    public Result<Integer> clean(@RequestParam(required = false) Integer days) {
        return Result.success(jobLogService.clean(days));
    }
}
