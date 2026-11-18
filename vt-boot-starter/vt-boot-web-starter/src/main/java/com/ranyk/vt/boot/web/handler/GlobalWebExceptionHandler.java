package com.ranyk.vt.boot.web.handler;

import com.ranyk.vt.boot.base.constant.ResponseCodeEnum;
import com.ranyk.vt.boot.base.constant.UserErrorCodeEnum;
import com.ranyk.vt.boot.base.exception.BaseException;
import com.ranyk.vt.boot.base.exception.DataSourceException;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.exception.UserException;
import com.ranyk.vt.boot.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * CLASS_NAME: GlobalWebExceptionHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 全局异常监听处理类
 * <p>
 * 处理策略:
 * <ul>
 *     <li>用户未登录异常: 返回 HTTP 401, 便于前端与网关直接识别鉴权失败</li>
 *     <li>业务异常: 返回 HTTP 200, 由响应体中的 success/code/msg 表达业务语义</li>
 *     <li>系统异常: 返回 HTTP 500, 响应体只暴露通用提示, 内部异常细节仅记录在服务端日志中</li>
 * </ul>
 * </p>
 * @date: 2026-02-08
 */
@Slf4j
@ControllerAdvice
public class GlobalWebExceptionHandler {

    /**
     * 自定义用户异常处理器
     * <p>
     * 用户未登录属于鉴权失败, 以 HTTP 401 返回, 使前端与网关能够直接识别并触发重新登录流程;
     * 响应体不再回填异常原始信息, 避免内部细节泄露。
     * </p>
     *
     * @param userException 自定义用户异常
     * @return 封装的通用结果对象 {@link Result} 对象
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Result<String>> userExceptionHandler(UserException userException) {
        log.warn("Current UserException occurred => {}", userException.getMessage());
        boolean notLogin = UserErrorCodeEnum.NOT_LOGIN.getValue().equals(userException.getCode());
        HttpStatus httpStatus = notLogin ? HttpStatus.UNAUTHORIZED : HttpStatus.OK;
        return ResponseEntity.status(httpStatus)
                .body(Result.<String>builder()
                        .success(Boolean.FALSE)
                        .code(notLogin ? String.valueOf(HttpStatus.UNAUTHORIZED.value()) : ResponseCodeEnum.BIZ_ERROR.name())
                        .msg(userException.getMessage())
                        .data(null)
                        .build());
    }

    /**
     * 自定义业务异常处理器
     *
     * @param serviceException 自定义业务异常
     * @return 封装的通用结果对象 {@link Result} 对象
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Result<String>> serviceExceptionHandler(ServiceException serviceException) {
        log.warn("Current ServiceException occurred => {} ", serviceException.getMessage());
        return ResponseEntity.ok(Result.<String>builder()
                .success(Boolean.FALSE)
                .code(resolveBusinessCode(serviceException))
                .msg(serviceException.getMessage())
                .data(null)
                .build());
    }

    /**
     * 自定义数据源异常处理器
     *
     * @param dataSourceException 自定义数据源异常
     * @return 封装的通用结果对象 {@link Result} 对象
     */
    @ExceptionHandler(DataSourceException.class)
    public ResponseEntity<Result<String>> dataSourceExceptionHandler(DataSourceException dataSourceException) {
        log.warn("Current DataSourceException occurred => {} ", dataSourceException.getMessage());
        return ResponseEntity.ok(Result.<String>builder()
                .success(Boolean.FALSE)
                .code(resolveBusinessCode(dataSourceException))
                .msg(dataSourceException.getMessage())
                .data(null)
                .build());
    }

    /**
     * 业务异常兜底处理器 - 覆盖其余继承自 {@link BaseException} 的业务异常
     *
     * @param baseException 业务异常对象
     * @return 封装的通用结果对象 {@link Result} 对象
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Result<String>> baseExceptionHandler(BaseException baseException) {
        log.warn("Current business exception occurred => {} ", baseException.getMessage());
        return ResponseEntity.ok(Result.<String>builder()
                .success(Boolean.FALSE)
                .code(resolveBusinessCode(baseException))
                .msg(baseException.getMessage())
                .data(null)
                .build());
    }

    /**
     * 系统异常处理器 - 兜底处理所有未被上面捕获的异常
     * <p>
     * 系统异常通常意味着程序缺陷或基础设施故障, 必须以 HTTP 500 暴露, 以便网关、监控与告警系统能够识别;
     * 同时响应体不得携带异常原始信息, 避免 SQL 语句、文件路径等内部细节泄露到前端。
     * </p>
     *
     * @param exception 异常对象
     * @return 封装的通用结果对象 {@link Result} 对象
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> exceptionHandler(Exception exception) {
        log.error("Current Exception occurred => {} ", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.<String>builder()
                        .success(Boolean.FALSE)
                        .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .msg(ResponseCodeEnum.SYSTEM_ERROR.name())
                        .data(null)
                        .build());
    }

    /**
     * 解析业务异常对应的业务错误码
     * <p>
     * 优先使用异常自身携带的错误码; 若异常仅携带了描述信息而没有错误码, 则统一归类为业务错误。
     * </p>
     *
     * @param baseException 业务异常对象
     * @return 业务错误码
     */
    private String resolveBusinessCode(BaseException baseException) {
        if (baseException.getCode() != null && !baseException.getCode().isBlank()) {
            return baseException.getCode();
        }
        return ResponseCodeEnum.BIZ_ERROR.name();
    }
}