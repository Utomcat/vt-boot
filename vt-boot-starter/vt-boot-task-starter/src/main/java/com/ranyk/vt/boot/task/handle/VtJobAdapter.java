package com.ranyk.vt.boot.task.handle;

import cn.hutool.extra.spring.SpringUtil;
import com.ranyk.vt.boot.base.context.TenantContext;
import com.ranyk.vt.boot.base.context.UserContext;
import com.ranyk.vt.boot.task.api.VtJobExecutor;
import com.ranyk.vt.boot.task.domain.VtJobContext;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;

/**
 * CLASS_NAME: VtJobAdapter.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Quartz 任务适配器 - 把 Quartz 的 Job 执行委托给容器中的 VtJobExecutor
 * <p>
 * Quartz 线程是复用的, 因此任务执行前后必须成对设置与清理租户/用户上下文,
 * 否则会出现"上一个任务的租户串到下一个任务"的问题。
 * 本类不跨线程自动传播上下文。
 * </p>
 * @date: 2026-11-18
 */
@Slf4j
public class VtJobAdapter implements Job {

    /**
     * JobDataMap 中执行器 Bean 名称的键
     */
    public static final String EXECUTOR_BEAN_KEY = "vtJobExecutorBean";

    /**
     * JobDataMap 中任务上下文的键
     */
    public static final String JOB_CONTEXT_KEY = "vtJobContext";

    /**
     * 执行任务
     *
     * @param context Quartz 任务执行上下文 {@link JobExecutionContext}
     * @throws JobExecutionException 任务执行异常
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        var data = context.getMergedJobDataMap();
        String executorBean = data.getString(EXECUTOR_BEAN_KEY);
        if (executorBean == null || executorBean.isBlank()) {
            throw new JobExecutionException("任务适配器未获取到执行器 Bean 名称");
        }
        VtJobExecutor executor;
        try {
            executor = SpringUtil.getBean(executorBean, VtJobExecutor.class);
        } catch (Exception e) {
            throw new JobExecutionException("任务执行器 Bean 不存在: " + executorBean, e);
        }
        VtJobContext storedContext = resolveJobContext(data);
        VtJobContext vtJobContext = VtJobContext.builder()
                .jobName(storedContext.getJobName())
                .jobGroup(storedContext.getJobGroup())
                .tenantId(storedContext.getTenantId())
                .params(storedContext.getParams())
                .scheduledFireTime(format(context.getScheduledFireTime()))
                .fireTime(format(context.getFireTime()))
                .refireCount(context.getRefireCount())
                .build();
        String tenantId = vtJobContext.getTenantId();
        try {
            if (tenantId != null && !tenantId.isBlank()) {
                TenantContext.setTenantId(tenantId);
            }
            log.debug("VtJob start execute. job: {}, group: {}", vtJobContext.getJobName(), vtJobContext.getJobGroup());
            executor.execute(vtJobContext);
            log.debug("VtJob execute finished. job: {}", vtJobContext.getJobName());
        } catch (Exception e) {
            log.error("VtJob execute error. job: {}", vtJobContext.getJobName(), e);
            throw new JobExecutionException(e, false);
        } finally {
            TenantContext.clear();
            UserContext.clear();
        }
    }

    /**
     * 把日期格式化为可读字符串
     *
     * @param date 日期对象
     * @return 格式化后的时间字符串, 入参为 null 时返回 null
     */
    private String format(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    /**
     * 从 JobDataMap 解析任务上下文
     *
     * @param data JobDataMap 对象 {@link org.quartz.JobDataMap}
     * @return 任务上下文 {@link VtJobContext}
     */
    private VtJobContext resolveJobContext(org.quartz.JobDataMap data) {
        Object value = data.get(JOB_CONTEXT_KEY);
        if (value instanceof VtJobContext vtJobContext) {
            return vtJobContext;
        }
        Map<String, Object> params = data.getWrappedMap();
        return VtJobContext.builder()
                .params(params)
                .build();
    }
}
