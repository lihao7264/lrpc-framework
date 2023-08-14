package com.atlihao.lrpc.framework.core.filter.server;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.annotations.SPI;
import com.atlihao.lrpc.framework.core.common.exception.LRpcException;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;
import com.atlihao.lrpc.framework.core.server.ServiceWrapper;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;
import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.PROVIDER_SERVICE_WRAPPER_MAP;

/**
 * @Description: 简单版本的token校验
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:45 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:45 下午
 * @Version: 1.0.0
 */
@SPI("before")
public class ServerTokenFilterImpl implements LServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        String token = String.valueOf(rpcInvocation.getAttachments().get("serviceToken"));
        ServiceWrapper serviceWrapper = PROVIDER_SERVICE_WRAPPER_MAP.get(rpcInvocation.getTargetServiceName());
        String matchToken = String.valueOf(serviceWrapper.getServiceToken());
        if (CommonUtils.isEmpty(matchToken)) {
            return;
        }
        if (!CommonUtils.isEmpty(token) && token.equals(matchToken)) {
            return;
        }
        rpcInvocation.setRetry(0);
        rpcInvocation.setE(new RuntimeException("service token is illegal for service " + rpcInvocation.getTargetServiceName()));
        rpcInvocation.setResponse(null);
        // 直接交给响应线程那边处理（响应线程在代理类内部的invoke函数中，那边会取出对应的uuid的值，然后判断）
        RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
        throw new LRpcException(rpcInvocation);
    }
}
