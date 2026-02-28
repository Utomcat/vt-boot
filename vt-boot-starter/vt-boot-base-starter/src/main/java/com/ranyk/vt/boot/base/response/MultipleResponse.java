package com.ranyk.vt.boot.base.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * CLASS_NAME: MultipleResponse.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 多结果响应属性封装对象,封装属性如下:
 * <p>
 *     <ul>
 *         <li>响应数据结果 List 集合: data</li>
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
public class MultipleResponse<T> extends BaseResponse<T> {
    @Serial
    private static final long serialVersionUID = -90369435163842250L;

    /**
     * 响应数据结果 List 集合
     */
    private List<T> data;
}
