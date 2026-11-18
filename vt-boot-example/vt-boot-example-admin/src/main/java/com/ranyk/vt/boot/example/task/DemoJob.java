package com.ranyk.vt.boot.example.task;

import com.ranyk.vt.boot.task.annotation.VtJob;
import com.ranyk.vt.boot.task.api.VtJobExecutor;
import com.ranyk.vt.boot.task.domain.VtJobContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: DemoJob.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 示例定时任务 - 每 30 秒输出一次任务上下文, 用于验证任务内核
 * @date: 2026-11-18
 */
@Slf4j
@Component
@VtJob(name = "demo-job", group = "DEFAULT", cron = "0/30 * * * * ?", description = "示例: 每 30 秒执行一次")
public class DemoJob implements VtJobExecutor {

    /**
     * 执行任务
     *
     * @param context 任务执行上下文 {@link VtJobContext}
     */
    @Override
    public void execute(VtJobContext context) {
        log.info("DemoJob executed. job: {}, fireTime: {}, params: {}", context.getJobName(), context.getFireTime(), context.getParams());
    }
}
