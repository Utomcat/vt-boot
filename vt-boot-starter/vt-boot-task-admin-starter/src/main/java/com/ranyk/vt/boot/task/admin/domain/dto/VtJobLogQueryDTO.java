package com.ranyk.vt.boot.task.admin.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: VtJobLogQueryDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志分页查询参数
 * @date: 2026-11-18
 */
@Data
public class VtJobLogQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5623071943876630019L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 状态: 0-执行中 1-成功 2-失败 3-被否决
     */
    private Integer status;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime from;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime to;
}
