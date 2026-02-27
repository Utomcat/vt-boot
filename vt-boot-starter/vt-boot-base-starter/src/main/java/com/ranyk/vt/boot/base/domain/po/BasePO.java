package com.ranyk.vt.boot.base.domain.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: BasePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 基础数据请求接收参数对象
 * @date: 2026-02-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(callSuper=true)
@SuppressWarnings("unused")
public class BasePO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3767708318679260L;

    /**
     * 主键ID 雪花算法生成
     */
    private String id;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    private Integer status;
    /**
     * 备注, 默认值为空字符串
     */
    private String remark;
    /**
     * 创建时间, 默认值为当前时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 创建人 ID, 默认值为 1
     */
    private String  createBy;
    /**
     * 更新时间, 默认值为当前时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 更新人 ID, 默认值为 1
     */
    private String updateBy;
}
