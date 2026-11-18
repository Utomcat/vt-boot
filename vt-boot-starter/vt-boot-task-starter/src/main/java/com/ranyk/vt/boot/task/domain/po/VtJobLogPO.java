package com.ranyk.vt.boot.task.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: VtJobLogPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志持久化对象, 对应表 vt_job_log
 * @date: 2026-11-18
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VtJobLogPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7441190355684027033L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 触发器名称
     */
    private String triggerName;

    /**
     * 计划触发时间
     */
    private LocalDateTime fireTime;

    /**
     * 实际开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 耗时(毫秒)
     */
    private Long durationMs;

    /**
     * 状态, 取值见 JobLogStatusEnum: 0-运行中 1-成功 2-失败 3-被否决
     */
    private Integer status;

    /**
     * 任务参数(JSON)
     */
    private String params;

    /**
     * 异常信息
     */
    private String errorMsg;

    /**
     * 执行实例标识
     */
    private String instanceId;

    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
