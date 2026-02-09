package com.ranyk.vt.boot.base.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: BaseRequest.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 请求对象基本属性封装对象
 * @date: 2026-02-07
 */
@Data
@ToString
@SuperBuilder
@EqualsAndHashCode
@AllArgsConstructor
public class BaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7317533349562741700L;
}
