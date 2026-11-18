package com.ranyk.vt.boot.task.util;

import cn.hutool.extra.spring.SpringUtil;
import com.ranyk.vt.boot.base.constant.TaskErrorCodeEnum;
import com.ranyk.vt.boot.base.exception.TaskException;
import com.ranyk.vt.boot.task.constant.MisfirePolicyEnum;
import com.ranyk.vt.boot.task.domain.VtJobContext;
import com.ranyk.vt.boot.task.handle.VtJobAdapter;
import com.ranyk.vt.boot.task.properties.VtJobDefinition;
import com.ranyk.vt.boot.task.properties.VtTaskProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CLASS_NAME: VtTaskUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 定时任务操作工具类 - 提供注册、查询、暂停、恢复、立即执行、删除能力
 * @date: 2026-11-18
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VtTaskUtils {

    /**
     * 默认任务分组
     */
    private static final String DEFAULT_GROUP = "DEFAULT";

    /**
     * 触发器标识后缀
     */
    private static final String TRIGGER_SUFFIX = "-trigger";

    /**
     * 获取调度器对象
     *
     * @return 调度器对象 {@link Scheduler}
     */
    private static Scheduler scheduler() {
        return SpringUtil.getBean(Scheduler.class);
    }

    /**
     * 新增或更新任务
     *
     * @param definition 任务定义 {@link VtJobDefinition}
     * @param overwrite  任务已存在时是否覆盖
     */
    public static void addOrUpdate(VtJobDefinition definition, boolean overwrite) {
        if (definition.getName() == null || definition.getName().isBlank()) {
            throw new TaskException(TaskErrorCodeEnum.TASK_NAME_NOT_BLANK.getValue(), new Object[0]);
        }
        if (definition.getCron() == null || definition.getCron().isBlank()) {
            throw new TaskException(TaskErrorCodeEnum.TASK_CRON_INVALID.getValue(), new Object[]{definition.getCron()});
        }
        JobKey jobKey = jobKey(definition.getGroup(), definition.getName());
        try {
            CronScheduleBuilder scheduleBuilder;
            try {
                scheduleBuilder = CronScheduleBuilder.cronSchedule(definition.getCron());
            } catch (Exception e) {
                throw new TaskException(TaskErrorCodeEnum.TASK_CRON_INVALID.getValue(), new Object[]{definition.getCron()});
            }
            if (scheduler().checkExists(jobKey)) {
                if (!overwrite) {
                    throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"任务已存在: " + definition.getName()});
                }
                scheduler().deleteJob(jobKey);
            }
            String tenantId = definition.getTenantId();
            if (tenantId == null || tenantId.isBlank()) {
                tenantId = taskProperties().getTenantId();
            }
            VtJobContext jobContext = VtJobContext.builder()
                    .jobName(definition.getName())
                    .jobGroup(defaultGroup(definition.getGroup()))
                    .tenantId(tenantId)
                    .params(definition.getParams() == null ? new HashMap<>() : definition.getParams())
                    .build();
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(VtJobAdapter.EXECUTOR_BEAN_KEY, definition.getExecutor());
            dataMap.put(VtJobAdapter.JOB_CONTEXT_KEY, jobContext);
            JobDetail jobDetail = JobBuilder.newJob(VtJobAdapter.class)
                    .withIdentity(jobKey)
                    .withDescription(definition.getDescription())
                    .usingJobData(dataMap)
                    .storeDurably()
                    .build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(definition.getName() + TRIGGER_SUFFIX, defaultGroup(definition.getGroup()))
                    .forJob(jobDetail)
                    .withSchedule(applyMisfire(scheduleBuilder, definition.getMisfire()))
                    .build();
            scheduler().scheduleJob(jobDetail, trigger);
            log.debug("VtTask saved. name: {}, group: {}, cron: {}", definition.getName(), definition.getGroup(), definition.getCron());
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"save"});
        }
    }

    /**
     * 查询全部任务定义
     *
     * @return 任务定义列表
     */
    public static List<VtJobDefinition> list() {
        List<VtJobDefinition> result = new ArrayList<>();
        try {
            Set<JobKey> keys = scheduler().getJobKeys(GroupMatcher.anyJobGroup());
            for (JobKey key : keys) {
                VtJobDefinition definition = toDefinition(key);
                if (definition != null) {
                    result.add(definition);
                }
            }
            result.sort((a, b) -> {
                int groupCompare = String.valueOf(a.getGroup()).compareTo(String.valueOf(b.getGroup()));
                return groupCompare != 0 ? groupCompare : String.valueOf(a.getName()).compareTo(String.valueOf(b.getName()));
            });
            return result;
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"list"});
        }
    }

    /**
     * 查询单个任务定义
     *
     * @param group 任务分组
     * @param name  任务名称
     * @return 任务定义, 不存在返回 null
     */
    public static VtJobDefinition get(String group, String name) {
        try {
            JobKey jobKey = jobKey(group, name);
            if (!scheduler().checkExists(jobKey)) {
                return null;
            }
            return toDefinition(jobKey);
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"get"});
        }
    }

    /**
     * 判断任务是否存在
     *
     * @param group 任务分组
     * @param name  任务名称
     * @return true: 存在; false: 不存在
     */
    public static boolean exists(String group, String name) {
        try {
            return scheduler().checkExists(jobKey(group, name));
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"checkExists"});
        }
    }

    /**
     * 判断任务是否处于暂停状态
     *
     * @param group 任务分组
     * @param name  任务名称
     * @return true: 已暂停; false: 运行中
     */
    public static boolean isPaused(String group, String name) {
        try {
            JobKey jobKey = jobKey(group, name);
            List<? extends Trigger> triggers = scheduler().getTriggersOfJob(jobKey);
            if (triggers.isEmpty()) {
                return false;
            }
            return scheduler().getTriggerState(triggers.get(0).getKey()) == Trigger.TriggerState.PAUSED;
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"state"});
        }
    }

    /**
     * 暂停任务
     *
     * @param group 任务分组
     * @param name  任务名称
     */
    public static void pause(String group, String name) {
        try {
            ensureExists(group, name);
            scheduler().pauseJob(jobKey(group, name));
            log.debug("VtTask paused. group: {}, name: {}", group, name);
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"pause"});
        }
    }

    /**
     * 恢复任务
     *
     * @param group 任务分组
     * @param name  任务名称
     */
    public static void resume(String group, String name) {
        try {
            ensureExists(group, name);
            scheduler().resumeJob(jobKey(group, name));
            log.debug("VtTask resumed. group: {}, name: {}", group, name);
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"resume"});
        }
    }

    /**
     * 立即执行一次
     *
     * @param group 任务分组
     * @param name  任务名称
     */
    public static void triggerNow(String group, String name) {
        try {
            ensureExists(group, name);
            scheduler().triggerJob(jobKey(group, name));
            log.debug("VtTask triggered. group: {}, name: {}", group, name);
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"trigger"});
        }
    }

    /**
     * 删除任务
     *
     * @param group 任务分组
     * @param name  任务名称
     * @return true: 删除成功; false: 任务不存在
     */
    public static boolean delete(String group, String name) {
        try {
            ensureExists(group, name);
            return scheduler().deleteJob(jobKey(group, name));
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"delete"});
        }
    }

    /**
     * 查询指定分组下的任务名称列表
     *
     * @param group 任务分组
     * @return 任务名称列表
     */
    public static List<String> listJobNames(String group) {
        try {
            return scheduler().getJobKeys(GroupMatcher.jobGroupEquals(defaultGroup(group)))
                    .stream()
                    .map(JobKey::getName)
                    .sorted()
                    .toList();
        } catch (SchedulerException e) {
            throw new TaskException(TaskErrorCodeEnum.TASK_OPERATION_FAIL.getValue(), new Object[]{"list"});
        }
    }

    /**
     * 把 Quartz 任务转换为任务定义
     *
     * @param jobKey 任务键 {@link JobKey}
     * @return 任务定义, 非框架任务返回 null
     * @throws SchedulerException 调度器异常
     */
    private static VtJobDefinition toDefinition(JobKey jobKey) throws SchedulerException {
        JobDetail jobDetail = scheduler().getJobDetail(jobKey);
        if (jobDetail == null || !VtJobAdapter.class.isAssignableFrom(jobDetail.getJobClass())) {
            return null;
        }
        VtJobDefinition definition = new VtJobDefinition();
        definition.setName(jobKey.getName());
        definition.setGroup(jobKey.getGroup());
        definition.setDescription(jobDetail.getDescription());
        JobDataMap dataMap = jobDetail.getJobDataMap();
        definition.setExecutor(dataMap.getString(VtJobAdapter.EXECUTOR_BEAN_KEY));
        Object contextValue = dataMap.get(VtJobAdapter.JOB_CONTEXT_KEY);
        if (contextValue instanceof VtJobContext jobContext) {
            definition.setParams(jobContext.getParams());
            definition.setTenantId(jobContext.getTenantId());
        }
        for (Trigger trigger : scheduler().getTriggersOfJob(jobKey)) {
            if (trigger instanceof CronTrigger cronTrigger) {
                definition.setCron(cronTrigger.getCronExpression());
                break;
            }
        }
        return definition;
    }

    /**
     * 校验任务存在
     *
     * @param group 任务分组
     * @param name  任务名称
     * @throws SchedulerException 调度器异常
     */
    private static void ensureExists(String group, String name) throws SchedulerException {
        if (!scheduler().checkExists(jobKey(group, name))) {
            throw new TaskException(TaskErrorCodeEnum.TASK_NOT_EXISTS.getValue(), new Object[]{name});
        }
    }

    /**
     * 按错过触发策略设置 cron 调度器
     *
     * @param builder cron 调度器构建器
     * @param misfire 错过触发策略 {@link MisfirePolicyEnum}
     * @return 配置好策略的构建器
     */
    private static CronScheduleBuilder applyMisfire(CronScheduleBuilder builder, MisfirePolicyEnum misfire) {
        if (misfire == MisfirePolicyEnum.FIRE_AND_PROCEED) {
            return builder.withMisfireHandlingInstructionFireAndProceed();
        }
        if (misfire == MisfirePolicyEnum.IGNORE) {
            return builder.withMisfireHandlingInstructionIgnoreMisfires();
        }
        return builder.withMisfireHandlingInstructionDoNothing();
    }

    /**
     * 获取任务配置属性
     *
     * @return 任务配置属性对象 {@link VtTaskProperties}
     */
    private static VtTaskProperties taskProperties() {
        return SpringUtil.getBean(VtTaskProperties.class);
    }

    /**
     * 构建任务键
     *
     * @param group 任务分组
     * @param name  任务名称
     * @return 任务键 {@link JobKey}
     */
    private static JobKey jobKey(String group, String name) {
        return JobKey.jobKey(name, defaultGroup(group));
    }

    /**
     * 归一化任务分组
     *
     * @param group 任务分组
     * @return 归一化后的任务分组
     */
    private static String defaultGroup(String group) {
        return group == null || group.isBlank() ? DEFAULT_GROUP : group;
    }

    /**
     * 查询任务参数
     *
     * @param jobKey 任务键
     * @return 任务参数
     * @throws SchedulerException 调度器异常
     */
    public static Map<String, Object> params(JobKey jobKey) throws SchedulerException {
        JobDetail jobDetail = scheduler().getJobDetail(jobKey);
        if (jobDetail == null) {
            return new HashMap<>();
        }
        Object value = jobDetail.getJobDataMap().get(VtJobAdapter.JOB_CONTEXT_KEY);
        return value instanceof VtJobContext jobContext && jobContext.getParams() != null
                ? jobContext.getParams()
                : new HashMap<>();
    }
}
