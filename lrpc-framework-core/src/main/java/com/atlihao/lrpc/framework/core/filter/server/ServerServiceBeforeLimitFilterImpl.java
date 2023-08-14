package com.atlihao.lrpc.framework.core.filter.server;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.ServerServiceSemaphoreWrapper;
import com.atlihao.lrpc.framework.core.common.annotations.SPI;
import com.atlihao.lrpc.framework.core.common.exception.MaxServiceLimitRequestException;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;

/**
 * @Description: 服务端方法前置限流过滤器
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 8:17 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 8:17 下午
 * @Version: 1.0.0
 */
@Slf4j
@SPI("before")
public class ServerServiceBeforeLimitFilterImpl implements LServerFilter {
    /**
     * 执行核心过滤逻辑
     *
     * @param rpcInvocation
     */
    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String targetServiceName = rpcInvocation.getTargetServiceName();
        ServerServiceSemaphoreWrapper serverServiceSemaphoreWrapper = SERVER_SERVICE_SEMAPHORE_MAP.get(targetServiceName);
        // 从缓存中提取semaphore对象
        Semaphore semaphore = serverServiceSemaphoreWrapper.getSemaphore();
        boolean tryResult = semaphore.tryAcquire();
        if (!tryResult) {
            log.error("[ServerServiceBeforeLimitFilterImpl] {}'s max request is {},reject now", rpcInvocation.getTargetServiceName(), serverServiceSemaphoreWrapper.getMaxNums());
            MaxServiceLimitRequestException lRpcException = new MaxServiceLimitRequestException(rpcInvocation);
            rpcInvocation.setE(lRpcException);
            throw lRpcException;
        }
    }
}
