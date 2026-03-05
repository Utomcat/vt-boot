package com.ranyk.vt.boot.satoken.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.ranyk.vt.boot.satoken.config.properties.SaTokenProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * CLASS_NAME: TokenResponseInterceptor.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Token 响应拦截器
 * @date: 2026-03-05
 */
@Slf4j
@Component
public class TokenResponseInterceptor implements HandlerInterceptor {

    /**
     * SA-TOKEN 配置属性对象
     */
    private final SaTokenProperties saTokenProperties;

    /**
     * 构造函数
     *
     * @param saTokenProperties SA-TOKEN 配置属性对象
     */
    @Autowired
    public TokenResponseInterceptor(SaTokenProperties saTokenProperties) {
        this.saTokenProperties = saTokenProperties;
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
     */
    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable ModelAndView modelAndView) {
        log.debug("========== TokenResponseInterceptor.postHandle ==========");
        log.debug("Request URI: {}", request.getRequestURI());
        log.debug("Is Login: {}", StpUtil.isLogin());
        // 判断用户是否已登录
        if (StpUtil.isLogin()) {
            String tokenValue = StpUtil.getTokenInfo().getTokenValue();
            String tokenName = saTokenProperties.getTokenName();
            log.debug("Token Name: {}", tokenName);
            log.debug("Token Value: {}", tokenValue);
            // 将 token 添加到响应头中
            response.setHeader(tokenName, tokenValue);
            log.info("User id {} ✅ Token 已设置到响应头：{} = {}", StpUtil.getLoginId(), tokenName, tokenValue);
        } else {
            log.debug("User not logged in, skip setting token");
        }
        log.debug("====================================================");
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
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        log.debug("TokenResponseInterceptor.afterCompletion completed");
    }
}
