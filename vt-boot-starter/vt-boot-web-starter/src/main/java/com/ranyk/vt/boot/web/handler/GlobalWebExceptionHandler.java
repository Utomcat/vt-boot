package com.ranyk.vt.boot.web.handler;

import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CLASS_NAME: GlobalWebExceptionHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 全局异常监听处理类
 * @date: 2026-02-08
 */
@Slf4j
@ControllerAdvice
public class GlobalWebExceptionHandler {


    /**
     * 自定义业务异常处理器
     *
     * @param serviceException 自定义业务异常
     * @return 返回封装的通用结果对象 {@link Result} 对象
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public Result<String> exceptionHandler(ServiceException serviceException) {
        log.error("serviceException occurred => ", serviceException);
        Result<String> result = new Result<>();
        result.setSuccess(Boolean.FALSE);
        result.setCode(serviceException.getCode());
        result.setMsg(serviceException.getMessage());
        return result;
    }
}
