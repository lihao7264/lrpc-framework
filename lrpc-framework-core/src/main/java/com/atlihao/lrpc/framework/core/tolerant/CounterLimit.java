package com.atlihao.lrpc.framework.core.tolerant;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 7:47 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 7:47 下午
 * @Version: 1.0.0
 */
public abstract class CounterLimit {

    protected int limitCount;

    protected long limitTime;

    protected TimeUnit timeUnit;

    protected volatile boolean isLimited;

    protected abstract boolean tryCount();

}
