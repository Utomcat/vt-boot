package com.ranyk.vt.boot.base.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: PageRequest.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 分页请求通用属性封装对象
 * @date: 2026-02-07
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PageRequest extends BaseRequest {
    @Serial
    private static final long serialVersionUID = -1175101237192790749L;

    /**
     * 当前页码
     */
    @Builder.Default
    private Integer currentPage = 1;
    /**
     * 每页显示数量
     */
    @Builder.Default
    private Integer pageSize = 10;
}
