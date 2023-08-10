package com.atlihao.lrpc.framework.core.common;

import java.util.concurrent.atomic.AtomicLong;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

/**
 * @Description:
 * @Author: 仲阳-李豪
 * @CreateDate: 2023/8/10 9:29 上午
 * @UpdateUser: 仲阳-李豪
 * @UpdateDate: 2023/8/10 9:29 上午
 * @Version: 1.0.0
 */
public class ChannelFuturePollingRef {

    private AtomicLong referenceTimes = new AtomicLong(0);

    public ChannelFutureWrapper getChannelFutureWrapper(String serviceName) {
        // 轮训获取数据
        ChannelFutureWrapper[] arr = SERVICE_ROUTER_MAP.get(serviceName);
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }
}
