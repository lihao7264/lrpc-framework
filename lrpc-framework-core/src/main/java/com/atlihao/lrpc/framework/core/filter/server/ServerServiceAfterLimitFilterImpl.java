package com.atlihao.lrpc.framework.core.filter.server;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.ServerServiceSemaphoreWrapper;
import com.atlihao.lrpc.framework.core.common.annotations.SPI;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;

import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

/**
 * @Description: 服务端方法后置限流过滤器
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 8:16 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 8:16 下午
 * @Version: 1.0.0
 */
@SPI("after")
public class ServerServiceAfterLimitFilterImpl implements LServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String serviceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(serviceName);
        serverServiceSemaphoreWrapper.getSemaphore().release();
    }

}
