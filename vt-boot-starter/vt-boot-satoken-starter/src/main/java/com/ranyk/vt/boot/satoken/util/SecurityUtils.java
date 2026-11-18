package com.ranyk.vt.boot.satoken.util;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.ranyk.vt.boot.base.constant.SessionKeyEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CLASS_NAME: SecurityUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 当前登录用户上下文工具类, 基于 Sa-Token 会话提供登录状态、用户ID、角色与管理员校验
 * @date: 2026-11-16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    /**
     * 管理员角色编码
     */
    private static final String ROLE_ADMIN = "ADMIN";

    /**
     * 是否已登录
     *
     * @return 是否已登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID, 未登录返回 null
     */
    public static Long getUserId() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        return Convert.toLong(StpUtil.getLoginIdAsString());
    }

    /**
     * 获取当前登录用户角色
     *
     * @return 角色编码, 未登录返回 null
     */
    public static String getRole() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        SaSession session = StpUtil.getSession(false);
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        Object role = session.get(SessionKeyEnum.ROLE.getValue());
        return ObjectUtil.isNull(role) ? null : Convert.toStr(role);
    }

    /**
     * 当前登录用户是否为管理员
     *
     * @return 是否管理员
     */
    public static boolean isAdmin() {
        return ObjectUtil.equal(ROLE_ADMIN, getRole());
    }

    /**
     * 要求当前登录用户具备管理员角色, 否则抛出业务异常
     */
    public static void requireAdmin() {
        if (!isAdmin()) {
            throw new ServiceException("无权限, 该操作需要管理员角色");
        }
    }
}
