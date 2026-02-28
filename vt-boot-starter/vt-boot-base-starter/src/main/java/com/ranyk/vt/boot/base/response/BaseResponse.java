package com.ranyk.vt.boot.base.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: BaseResponse.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 基本响应属性封装对象,封装属性如下:
 * <p>
 *     <ul>
 *         <li>响应结果: success</li>
 *         <li>响应码: code</li>
 *         <li>响应信息: msg</li>
 *     </ul>
 * </p>
 * @date: 2026-02-07
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@SuppressWarnings("unused")
public class BaseResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1165419281884329145L;

    /**
     * 响应结果
     */
    private Boolean success;
    /**
     * 响应码
     */
    private String code;
    /**
     * 响应信息
     */
    private String msg;
}
