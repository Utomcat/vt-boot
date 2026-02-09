package com.ranyk.vt.boot.base.exception;

import lombok.*;

import java.io.Serial;

/**
 * CLASS_NAME: ServiceException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 自定义业务异常类
 * @date: 2026-02-07
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends BaseException{

    @Serial
    private static final long serialVersionUID = -4063634724930884784L;


}
