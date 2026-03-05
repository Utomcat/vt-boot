package com.ranyk.vt.boot.satoken.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.ranyk.vt.boot.base.constant.TenantEnum;
import com.ranyk.vt.boot.base.context.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * CLASS_NAME: TenantWebInterceptor.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 多租户全局拦截器
 * @date: 2026-03-04
 */
@Slf4j
@Component
public class TenantWebInterceptor extends SaInterceptor {

    /**
     * 是否启用多租户功能
     */
    private final Boolean isEnableTenant;
    /**
     * 排除的路径模式列表，默认为 /login (登录接口请求), /static/** (静态资源请求), /favicon.ico (图标请求), /api/captcha (验证码接口请求)
     */
    private final List<String> excludePathPatterns;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入 是否启用多租户功能
     *
     * @param isEnableTenant 是否启用多租户功能
     * @param excludePathPatterns 排除的路径模式列表
     */
    @Autowired
    public TenantWebInterceptor(Boolean isEnableTenant, List<String> excludePathPatterns) {
        this.isEnableTenant = isEnableTenant;
        this.excludePathPatterns = excludePathPatterns;
    }

    /**
     * Interception point before the execution of a handler. Called after
     * HandlerMapping determined an appropriate handler object, but before
     * HandlerAdapter invokes the handler.
     * <p>DispatcherServlet processes a handler in an execution chain, consisting
     * of any number of interceptors, with the handler itself at the end.
     * With this method, each interceptor can decide to abort the execution chain,
     * typically sending an HTTP error or writing a custom response.
     * <p><strong>Note:</strong> special considerations apply for asynchronous
     * request processing. For more details see
     * {@link AsyncHandlerInterceptor}.
     * <p>The default implementation returns {@code true}.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        // 从配置文件中获取 vt.boot.is-enable-tenant 配置项
        log.debug("当前系统中配置的是否启用租户功能: {}", isEnableTenant);
        // 若未启用多租户, 则直接返回 true
        if (!isEnableTenant) {
            log.debug("多租户功能未启用，跳过租户 ID 检查");
            return true;
        }
        // 添加排除路径检查, 若当前请求路径被排除, 则直接返回 true
        if (excludePathPatterns.contains(request.getRequestURI())){
            log.debug("当前请求 {} 已被排除，跳过租户 ID 检查", request.getRequestURI());
            return true;
        }
        // 定义租户ID
        String tenantId = "";
        // 已登录的状态, 从会话 Session 中获取当前用户的租户ID
        if (StpUtil.isLogin()) {
            tenantId = Optional.ofNullable(String.valueOf(StpUtil.getSession().get(TenantEnum.TENANT_ID.getValue()))).filter(StrUtil::isNotBlank).orElse("");
        }
        // 未登录的状态, 从请求参数中获取当前用户的租户ID
        if (StrUtil.isBlank(tenantId)) {
            Object tenantIdObj = request.getAttribute(TenantEnum.TENANT_ID.getValue());
            tenantId = Objects.isNull(tenantIdObj) ? "" : String.valueOf(tenantIdObj);
        }
        // 若租户ID不为空, 则将其设置到 TenantContext 中
        if (StrUtil.isNotBlank(tenantId)) {
            TenantContext.setTenantId(tenantId);
            return true;
        }
        else {
            log.error("请求 {} 未指定租户ID", request.getRequestURI());
            return false;
        }
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
        log.debug("========== TenantWebInterceptor.postHandle ==========");
        log.debug("Request URI: {}", request.getRequestURI());
        log.debug("Request Method: {}", request.getMethod());
        log.debug("Is Enable Tenant: {}", isEnableTenant);
        log.debug("Is Login: {}", StpUtil.isLogin());

        if (isEnableTenant && StpUtil.isLogin()){
            // 添加租户ID
            Object tenantId = StpUtil.getSession().get(TenantEnum.TENANT_ID.getValue());
            log.debug("Tenant ID from Session: {}", tenantId);

            if (Objects.nonNull(tenantId)){
                String headerKey = TenantEnum.TENANT_ID.getValue();
                String headerValue = String.valueOf(tenantId);

                log.debug("Setting Header Key: {}", headerKey);
                log.debug("Setting Header Value: {}", headerValue);

                response.setHeader(TenantEnum.TENANT_ID.getValue(), String.valueOf(tenantId));
                log.info("User ID - {}✅ 租户 ID 已设置到响应头：{} = {}", StpUtil.getLoginId(), headerKey, headerValue);
            } else {
                log.warn("⚠️ Tenant ID is null in session");
            }
        } else {
            log.debug("Skip setting tenant ID - isEnableTenant: {} or isLogin: {}", isEnableTenant, StpUtil.isLogin());
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
        log.debug("请求 {} 处理完成，清除租户 ID", request.getRequestURI());
        TenantContext.clear();
    }
}
