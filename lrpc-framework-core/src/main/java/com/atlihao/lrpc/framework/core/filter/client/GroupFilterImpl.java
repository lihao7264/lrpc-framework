package com.atlihao.lrpc.framework.core.filter.client;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.filter.LClientFilter;

import java.util.Iterator;
import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;

/**
 * @Description: 基于分组的过滤链路
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:43 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:43 下午
 * @Version: 1.0.0
 */
public class GroupFilterImpl implements LClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        String group = String.valueOf(rpcInvocation.getAttachments().get("group"));
        Iterator<ChannelFutureWrapper> channelFutureWrapperIterator = src.iterator();
        while (channelFutureWrapperIterator.hasNext()) {
            ChannelFutureWrapper channelFutureWrapper = channelFutureWrapperIterator.next();
            if (!channelFutureWrapper.getGroup().equals(group)) {
                channelFutureWrapperIterator.remove();
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            rpcInvocation.setRetry(0);
            rpcInvocation.setE(new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in group " + group));
            rpcInvocation.setResponse(null);
            //直接交给响应线程那边处理（响应线程在代理类内部的invoke函数中，则会取出对应的uuid值，然后判断）
            RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
            throw new RuntimeException("no provider match for service " + rpcInvocation.getTargetServiceName() + " in group " + group);
        }
    }
}

