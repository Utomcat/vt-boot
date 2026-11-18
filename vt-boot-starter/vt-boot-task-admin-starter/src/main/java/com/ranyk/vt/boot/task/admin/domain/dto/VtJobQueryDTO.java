package com.ranyk.vt.boot.task.admin.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: VtJobQueryDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务分页查询参数
 * @date: 2026-11-18
 */
@Data
public class VtJobQueryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6214553174328652115L;

    /**
     * 当前页码, 默认 1
     */
    private Integer page = 1;
    /**
     * 每页条数, 默认 10
     */
    private Integer size = 10;
    /**
     * 任务名称, 模糊匹配
     */
    private String name;
    /**
     * 任务分组
     */
    private String group;
}
