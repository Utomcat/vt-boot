package com.ranyk.vt.boot.satoken.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.ranyk.vt.boot.base.context.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * CLASS_NAME: TokenRequestInterceptor.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Token 请求 Sa-Token 拦截器, 用于获取当前会话中的上下文中的一些固定属性
 * @date: 2026-04-03
 */
@Slf4j
@Component
public class TokenRequestInterceptor extends SaInterceptor {

    /**
     * 每次请求之前触发的方法
     *
     * @param request  当前请求的 {@link HttpServletRequest} 对象
     * @param response 当前请求的 {@link HttpServletResponse} 对象
     * @param handler  当前请求的 {@link HandlerMethod} 对象
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.trace("========== TokenRequestInterceptor.preHandle ==========");
        if (StpUtil.isLogin()) {
            String loginIdAsString = StpUtil.getLoginIdAsString();
            log.trace("当前登录账户的ID: {}", loginIdAsString);
            // 将当前登录账户的ID 设置到上下文, 用于后续在其他地方使用当前登录的账户 ID
            UserContext.setUserId(loginIdAsString);
        }
        log.trace("========== TokenRequestInterceptor.preHandle completed ==========");
        return true;
    }

    /**
     * Interception point after successful execution of a handler.
     * Called after HandlerAdapter actually invoked the handler, but before the
     * DispatcherServlet renders the view. Can expose additional model objects
     * to the view via the given ModelAndView.
     * <p>DispatcherServlet processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can post-process an execution,
     * getting applied in inverse order of the execution chain.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * {@link AsyncHandlerInterceptor}.
     * <p>The default implementation is empty.
     *
     * @param request      current HTTP request
     * @param response     current HTTP response
     * @param handler      the handler (or {@link HandlerMethod}) that started asynchronous
     *                     execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     *                     (can also be {@code null})
     * @throws Exception in case of errors
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.trace("========== TokenRequestInterceptor.postHandle ==========");
        log.trace("========== TokenRequestInterceptor.postHandle completed ==========");
    }

    /**
     * Callback after completion of request processing, that is, after rendering
     * the view. Will be called on any outcome of handler execution, thus allows
     * for proper resource cleanup.
     * <p>Note: Will only be called if this interceptor's {@code preHandle}
     * method has successfully completed and returned {@code true}!
     * <p>As with the {@code postHandle} method, the method will be invoked on each
     * interceptor in the chain in reverse order, so the first interceptor will be
     * the last to be invoked.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * {@link AsyncHandlerInterceptor}.
     * <p>The default implementation is empty.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  the handler (or {@link HandlerMethod}) that started asynchronous
     *                 execution, for type and/or instance examination
     * @param ex       any exception thrown on handler execution, if any; this does not
     *                 include exceptions that have been handled through an exception resolver
     * @throws Exception in case of errors
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        log.trace("========== TokenRequestInterceptor.afterCompletion ==========");
        log.trace("清除用户 ID 开始");
        UserContext.clear();
        log.trace("清除用户 ID 结束");
        log.trace("========== TokenRequestInterceptor.afterCompletion completed ==========");
    }
}
