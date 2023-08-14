package com.atlihao.lrpc.framework.core.filter.client;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.filter.LClientFilter;

import java.util.Iterator;
import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;

/**
 * @Description: 直连过滤器:指定ip+port
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:41 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:41 下午
 * @Version: 1.0.0
 */
public class DirectInvokeFilterImpl implements LClientFilter {
    /**
     * 执行过滤链
     *
     * @param src
     * @param rpcInvocation
     * @return
     */
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String url = (String) rpcInvocation.getAttachments().get("url");
        if (CommonUtils.isEmpty(url)) {
            return;
        }
        Iterator<ChannelFutureWrapper> channelFutureWrapperIterator = src.iterator();
        while (channelFutureWrapperIterator.hasNext()) {
            ChannelFutureWrapper channelFutureWrapper = channelFutureWrapperIterator.next();
            if (!(channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort()).equals(url)) {
                channelFutureWrapperIterator.remove();
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setE(new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName()  + " in url " + url));
            rpcInvocation.setResponse(null);
            // 直接交给响应线程那边处理（响应线程在代理类内部的invoke函数中，则会取出对应的uuid值，然后判断）
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            throw new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in url " + url);
        }
    }
}
