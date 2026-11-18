package com.ranyk.vt.boot.task.admin.controller;

import com.ranyk.vt.boot.task.admin.domain.dto.VtJobQueryDTO;
import com.ranyk.vt.boot.task.admin.domain.dto.VtJobSaveDTO;
import com.ranyk.vt.boot.task.admin.domain.vo.VtJobVO;
import com.ranyk.vt.boot.task.admin.service.VtJobService;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: VtJobController.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务管理接口, 提供任务的查询、新增、修改、暂停、恢复、立即执行与删除
 * @date: 2026-11-18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class VtJobController {

    /**
     * 任务管理业务对象
     */
    private final VtJobService jobService;

    /**
     * 分页查询任务列表。按任务名称模糊匹配、任务分组精确匹配, 未传条件时查询全部任务。
     *
     * @param queryDTO 分页查询参数, 包含页码、每页条数及可选筛选条件
     * @return 包含当前页任务列表与总数的分页结果
     */
    @GetMapping("/list")
    public MultiResult<VtJobVO> list(VtJobQueryDTO queryDTO) {
        return jobService.page(queryDTO);
    }

    /**
     * 查询任务详情。
     *
     * @param group 任务分组, 可为空
     * @param name  任务名称
     * @return 任务详细信息
     */
    @GetMapping("/detail")
    public Result<VtJobVO> detail(@RequestParam(required = false) String group, @RequestParam String name) {
        return Result.success(jobService.detail(group, name));
    }

    /**
     * 新增任务。校验 cron 与执行器后注册到调度器, 任务已存在时直接报错。
     *
     * @param saveDTO 任务保存参数
     * @return 操作结果
     */
    @PostMapping("/create")
    public Result<Void> create(@RequestBody VtJobSaveDTO saveDTO) {
        jobService.create(saveDTO);
        return Result.success(null);
    }

    /**
     * 修改任务。以最新参数覆盖重建任务, 任务不存在时直接报错。
     *
     * @param saveDTO 任务保存参数
     * @return 操作结果
     */
    @PutMapping("/update")
    public Result<Void> update(@RequestBody VtJobSaveDTO saveDTO) {
        jobService.update(saveDTO);
        return Result.success(null);
    }

    /**
     * 暂停任务。
     *
     * @param group 任务分组, 可为空
     * @param name  任务名称
     * @return 操作结果
     */
    @PutMapping("/pause")
    public Result<Void> pause(@RequestParam(required = false) String group, @RequestParam String name) {
        jobService.pause(group, name);
        return Result.success(null);
    }

    /**
     * 恢复任务。
     *
     * @param group 任务分组, 可为空
     * @param name  任务名称
     * @return 操作结果
     */
    @PutMapping("/resume")
    public Result<Void> resume(@RequestParam(required = false) String group, @RequestParam String name) {
        jobService.resume(group, name);
        return Result.success(null);
    }

    /**
     * 立即执行一次。不受 cron 影响, 立即触发一次任务执行。
     *
     * @param group 任务分组, 可为空
     * @param name  任务名称
     * @return 操作结果
     */
    @PutMapping("/trigger")
    public Result<Void> trigger(@RequestParam(required = false) String group, @RequestParam String name) {
        jobService.triggerNow(group, name);
        return Result.success(null);
    }

    /**
     * 删除任务。
     *
     * @param group 任务分组, 可为空
     * @param name  任务名称
     * @return 操作结果
     */
    @DeleteMapping("/delete")
    public Result<Void> delete(@RequestParam(required = false) String group, @RequestParam String name) {
        jobService.delete(group, name);
        return Result.success(null);
    }
}
