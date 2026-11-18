package com.ranyk.vt.boot.task.handle;

import cn.hutool.json.JSONUtil;
import com.ranyk.vt.boot.task.constant.JobLogStatusEnum;
import com.ranyk.vt.boot.task.domain.VtJobContext;
import com.ranyk.vt.boot.task.domain.po.VtJobLogPO;
import com.ranyk.vt.boot.task.repository.VtJobLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.SchedulerException;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * CLASS_NAME: VtJobListener.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Quartz 任务监听器 - 通过 VtJobLogRepository 记录任务执行日志(Quartz 本身不保存执行历史)
 * <p>
 * 只处理本框架的 {@link VtJobAdapter} 任务; 仓储实现内部已吞掉写入异常, 不会影响任务本身的执行结果。
 * </p>
 * @date: 2026-11-18
 */
@Slf4j
@RequiredArgsConstructor
public class VtJobListener implements JobListener {

    /**
     * 监听器名称
     */
    private static final String LISTENER_NAME = "vtJobLogListener";

    /**
     * JobExecutionContext 中保存日志主键的键
     */
    private static final String LOG_ID_KEY = "vtJobLogId";

    /**
     * 任务执行日志仓储对象
     */
    private final VtJobLogRepository jobLogRepository;

    /**
     * 获取监听器名称
     *
     * @return 监听器名称
     */
    @Override
    public String getName() {
        return LISTENER_NAME;
    }

    /**
     * 任务即将执行时写入"执行中"日志
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        if (!isVtJob(context)) {
            return;
        }
        context.put(LOG_ID_KEY, jobLogRepository.saveStart(buildLog(context, JobLogStatusEnum.RUNNING)));
    }

    /**
     * 任务被否决(未执行)时写入"被否决"日志
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        if (!isVtJob(context)) {
            return;
        }
        Long logId = jobLogRepository.saveStart(buildLog(context, JobLogStatusEnum.VETOED));
        jobLogRepository.markFinished(logId, JobLogStatusEnum.VETOED.getValue(), 0L, null);
        log.debug("VtJob vetoed. job: {}", context.getJobDetail().getKey().getName());
    }

    /**
     * 任务执行完成时回写最终状态
     *
     * @param context               Quartz 任务执行上下文 {@link JobExecutionContext}
     * @param jobExecutionException 任务执行异常, 无异常为 null
     */
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobExecutionException) {
        if (!isVtJob(context)) {
            return;
        }
        Object value = context.get(LOG_ID_KEY);
        Long logId = value instanceof Long id ? id : null;
        JobLogStatusEnum status = jobExecutionException == null ? JobLogStatusEnum.SUCCESS : JobLogStatusEnum.FAILED;
        long runTime = context.getJobRunTime();
        long duration = runTime < 0 ? 0L : runTime;
        String errorMsg = jobExecutionException == null ? null : jobExecutionException.getMessage();
        jobLogRepository.markFinished(logId, status.getValue(), duration, errorMsg);
    }

    /**
     * 根据 Quartz 上下文构建日志对象
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     * @param status  初始状态 {@link JobLogStatusEnum}
     * @return 日志对象 {@link VtJobLogPO}
     */
    private VtJobLogPO buildLog(JobExecutionContext context, JobLogStatusEnum status) {
        JobKey jobKey = context.getJobDetail().getKey();
        return VtJobLogPO.builder()
                .jobName(jobKey.getName())
                .jobGroup(jobKey.getGroup())
                .triggerName(context.getTrigger() == null ? null : context.getTrigger().getKey().getName())
                .fireTime(toLocalDateTime(context.getScheduledFireTime()))
                .startTime(toLocalDateTime(context.getFireTime()))
                .status(status.getValue())
                .params(resolveParamsJson(context))
                .instanceId(resolveInstanceId(context))
                .tenantId(resolveTenantId(context))
                .createTime(LocalDateTime.now())
                .build();
    }

    /**
     * 判断是否为框架任务
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     * @return true: 是框架任务; false: 不是
     */
    private boolean isVtJob(JobExecutionContext context) {
        return context.getJobDetail() != null
                && VtJobAdapter.class.isAssignableFrom(context.getJobDetail().getJobClass());
    }

    /**
     * 解析调度器实例标识
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     * @return 调度器实例标识
     */
    private String resolveInstanceId(JobExecutionContext context) {
        try {
            return context.getScheduler().getSchedulerInstanceId();
        } catch (SchedulerException e) {
            return null;
        }
    }

    /**
     * 解析租户 ID
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     * @return 租户 ID
     */
    private String resolveTenantId(JobExecutionContext context) {
        Object value = context.getMergedJobDataMap().get(VtJobAdapter.JOB_CONTEXT_KEY);
        if (value instanceof VtJobContext vtJobContext) {
            return vtJobContext.getTenantId();
        }
        return null;
    }

    /**
     * 解析任务参数 JSON
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     * @return 任务参数 JSON 字符串
     */
    private String resolveParamsJson(JobExecutionContext context) {
        Object value = context.getMergedJobDataMap().get(VtJobAdapter.JOB_CONTEXT_KEY);
        if (value instanceof VtJobContext vtJobContext && vtJobContext.getParams() != null) {
            return JSONUtil.toJsonStr(vtJobContext.getParams());
        }
        return null;
    }

    /**
     * 把日期转换为本地时间
     *
     * @param date 日期对象
     * @return 本地时间
     */
    private LocalDateTime toLocalDateTime(java.util.Date date) {
        return date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
