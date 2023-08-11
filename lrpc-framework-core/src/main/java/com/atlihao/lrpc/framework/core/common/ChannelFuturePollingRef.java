package com.atlihao.lrpc.framework.core.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 9:29 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 9:29 上午
 * @Version: 1.0.0
 */
public class ChannelFuturePollingRef {

    private AtomicLong referenceTimes = new AtomicLong(0);

    public ChannelFutureWrapper getChannelFutureWrapper(ChannelFutureWrapper[] arr) {
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }
}
