package com.ranyk.vt.boot.base.response;

import lombok.*;

import java.io.Serial;

/**
 * CLASS_NAME: SingleResponse.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 单结果响应属性封装对象,封装属性如下:
 * <p>
 *     <ul>
 *         <li>响应数据结果: data</li>
 *     </ul>
 * </p>
 * @date: 2026-02-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SingleResponse<T> extends BaseResponse {
    @Serial
    private static final long serialVersionUID = 5459415561490169322L;

    /**
     * 响应数据结果
     */
    private T data;
}
