package com.ranyk.vt.boot.task.admin.domain.vo;

import com.ranyk.vt.boot.task.constant.MisfirePolicyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * CLASS_NAME: VtJobVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务信息响应对象
 * @date: 2026-11-18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VtJobVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2277124355136160255L;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务分组
     */
    private String group;

    /**
     * cron 表达式
     */
    private String cron;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务执行器 Bean 名称
     */
    private String executor;

    /**
     * 错过触发的处理策略
     */
    private MisfirePolicyEnum misfire;

    /**
     * 任务参数
     */
    private Map<String, Object> params;

    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 是否已暂停
     */
    private Boolean paused;
}
