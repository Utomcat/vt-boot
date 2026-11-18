package com.ranyk.vt.boot.satoken.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.ranyk.vt.boot.base.constant.UserErrorCodeEnum;
import com.ranyk.vt.boot.base.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * CLASS_NAME: SaTokenExceptionHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Sa-Token 鉴权异常处理类
 * <p>
 * Sa-Token 在拦截器与权限注解中抛出的鉴权异常统一在此转换为标准响应:
 * 未登录返回 HTTP 401, 无权限返回 HTTP 403, 便于前端与网关直接识别鉴权失败场景。
 * </p>
 * @date: 2026-10-20
 */
@Slf4j
@RestControllerAdvice
public class SaTokenExceptionHandler {

    /**
     * 未登录异常处理器 - 返回 HTTP 401
     *
     * @param notLoginException Sa-Token 未登录异常
     * @return 标准响应对象 {@link BaseResponse} 对象
     */
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<BaseResponse<String>> notLoginExceptionHandler(NotLoginException notLoginException) {
        log.warn("Current NotLoginException occurred => {}", notLoginException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BaseResponse.<String>builder()
                        .success(Boolean.FALSE)
                        .code(UserErrorCodeEnum.NOT_LOGIN.getValue())
                        .msg(notLoginException.getMessage())
                        .build());
    }

    /**
     * 无权限异常处理器 - 返回 HTTP 403
     *
     * @param notPermissionException Sa-Token 无权限异常
     * @return 标准响应对象 {@link BaseResponse} 对象
     */
    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<BaseResponse<String>> notPermissionExceptionHandler(NotPermissionException notPermissionException) {
        log.warn("Current NotPermissionException occurred => {}", notPermissionException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.<String>builder()
                        .success(Boolean.FALSE)
                        .code(UserErrorCodeEnum.NOT_LOGIN.getValue())
                        .msg(notPermissionException.getMessage())
                        .build());
    }

    /**
     * 无角色异常处理器 - 返回 HTTP 403
     *
     * @param notRoleException Sa-Token 无角色异常
     * @return 标准响应对象 {@link BaseResponse} 对象
     */
    @ExceptionHandler(NotRoleException.class)
    public ResponseEntity<BaseResponse<String>> notRoleExceptionHandler(NotRoleException notRoleException) {
        log.warn("Current NotRoleException occurred => {}", notRoleException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(BaseResponse.<String>builder()
                        .success(Boolean.FALSE)
                        .code(UserErrorCodeEnum.NOT_LOGIN.getValue())
                        .msg(notRoleException.getMessage())
                        .build());
    }
}