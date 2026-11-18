package com.ranyk.vt.boot.task.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * CLASS_NAME: VtJobContext.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行上下文 - 传递任务名称、触发时间、参数与租户信息
 * @date: 2026-11-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VtJobContext implements Serializable {

    @Serial
    private static final long serialVersionUID = -3351786246935512705L;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 计划触发时间(格式化后展示用)
     */
    private String scheduledFireTime;

    /**
     * 实际触发时间(格式化后展示用)
     */
    private String fireTime;

    /**
     * 重试次数, 首次执行为 0
     */
    private Integer refireCount;

    /**
     * 租户 ID, 为空表示不切换租户上下文
     */
    private String tenantId;

    /**
     * 任务参数
     */
    private Map<String, Object> params;
}
