package com.ranyk.vt.boot.task.api;

import com.ranyk.vt.boot.task.domain.VtJobContext;

/**
 * CLASS_NAME: VtJobExecutor.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行器接口 - 业务任务只依赖该接口, 便于未来替换调度实现(如 xxl-job)而不改业务代码
 * @date: 2026-11-18
 */
public interface VtJobExecutor {

    /**
     * 执行任务
     *
     * @param context 任务执行上下文 {@link VtJobContext}
     */
    void execute(VtJobContext context);
}
