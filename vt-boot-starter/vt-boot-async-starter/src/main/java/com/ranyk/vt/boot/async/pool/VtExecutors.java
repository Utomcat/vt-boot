package com.ranyk.vt.boot.async.pool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * CLASS_NAME: VtExecutors.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 虚拟线程执行器
 * @date: 2026-02-09
 */
public final class VtExecutors {

    /**
     * 创建虚拟线程执行器
     *
     * @return 虚拟线程执行器对象 {@link Executor}
     */
    public static Executor newVirtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
