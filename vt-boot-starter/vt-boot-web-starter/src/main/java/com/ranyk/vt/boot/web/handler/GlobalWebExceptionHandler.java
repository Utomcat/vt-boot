package com.ranyk.vt.boot.web.handler;

import com.ranyk.vt.boot.base.exception.DataSourceException;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.exception.UserException;
import com.ranyk.vt.boot.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

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
        log.error("Current ServiceException occurred => {} ", serviceException.getMessage(), serviceException);
        return Result.<String>builder()
                .success(Boolean.FALSE)
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .msg(serviceException.getMessage())
                .data(serviceException.getMessage())
                .build();
    }

    /**
     * 自定义用户异常处理器
     *
     * @param userException 自定义用户异常
     * @return 封装的通用结果对象 {@link Result} 对象
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UserException.class)
    public Result<String> exceptionHandler(UserException userException) {
        log.error("Current UserException occurred => {}", userException.getMessage(), userException);
        return Result.<String>builder()
                .success(Boolean.FALSE)
                .code(Objects.equals(userException.getCode(), "user.not.login") ? String.valueOf(HttpStatus.UNAUTHORIZED.value()) : String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .msg(userException.getMessage())
                .data(userException.getMessage())
                .build();
    }


    /**
     * 自定义数据源异常处理器
     *
     * @param dataSourceException 自定义数据源异常
     * @return 封装的通用结果对象 {@link Result} 对象
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DataSourceException.class)
    public Result<String> exceptionHandler(DataSourceException dataSourceException) {
        log.error("Current DataSourceException occurred => {} ", dataSourceException.getMessage(), dataSourceException);
        return Result.<String>builder()
                .success(Boolean.FALSE)
                .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .msg(dataSourceException.getMessage())
                .data(dataSourceException.getMessage())
                .build();
    }

}
