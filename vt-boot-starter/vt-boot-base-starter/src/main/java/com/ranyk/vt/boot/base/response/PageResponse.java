package com.ranyk.vt.boot.base.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: PageResponse.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 分页结果响应属性封装对象,封装属性如下:
 * <p>
 *     <ul>
 *         <li>当前页码: currentPage</li>
 *         <li>每页显示数量: pageSize</li>
 *         <li>总页数: totalPage</li>
 *         <li>数据总数: total</li>
 *     </ul>
 * </p>
 * @date: 2026-02-07
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PageResponse<T> extends MultipleResponse<T> {
    @Serial
    private static final long serialVersionUID = 491410952901426830L;

    /**
     * 当前页码
     */
    @Builder.Default
    private Integer currentPage = 1;
    /**
     * 每页显示数量
     */
    @Builder.Default
    private Integer pageSize  = 10;
    /**
     * 总页数
     */
    @Builder.Default
    private Integer totalPage = 0;
    /**
     * 数据总数
     */
    @Builder.Default
    private Integer total = 0;
}
