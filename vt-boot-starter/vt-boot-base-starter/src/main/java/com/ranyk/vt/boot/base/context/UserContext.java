package com.ranyk.vt.boot.base.context;

/**
 * CLASS_NAME: UserContext.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户上下文监听类
 * @date: 2026-03-24
 */
public class UserContext {

    /**
     * 用户ID 线程存储本地变量
     */
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public static String getUserId() {
        return USER_ID.get();
    }

    /**
     * 清空用户ID
     */
    public static void clear() {
        USER_ID.remove();
    }
}
