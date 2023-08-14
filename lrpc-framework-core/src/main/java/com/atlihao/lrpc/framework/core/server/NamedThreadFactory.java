package com.atlihao.lrpc.framework.core.server;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:  重写线程工厂类.
 * @Author: lihao726726
 * @CreateDate: 2023/8/14 8:12 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/14 8:12 上午
 * @Version: 1.0.0
 */
public class NamedThreadFactory implements ThreadFactory {

    /**
     * 线程池的线程序号
     */
    protected static final AtomicInteger POOL_SEQ = new AtomicInteger(1);


    protected final AtomicInteger mThreadNum = new AtomicInteger(1);

    /**
     * 线程工厂的线程名前缀
     */
    protected final String mPrefix;

    /**
     * 线程工厂的线程是否是守护线程
     */
    protected final boolean mDaemon;

    /**
     * 线程组
     */
    protected final ThreadGroup mGroup;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement(), false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        mPrefix = prefix + "-thread-";
        mDaemon = daemon;
        SecurityManager s = System.getSecurityManager();
        mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(mGroup, runnable, name, 0);
        ret.setDaemon(mDaemon);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return mGroup;
    }
}

