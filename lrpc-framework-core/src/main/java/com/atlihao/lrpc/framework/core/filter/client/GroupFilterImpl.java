package com.atlihao.lrpc.framework.core.filter.client;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.filter.LClientFilter;

import java.util.Iterator;
import java.util.List;

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
        Iterator<ChannelFutureWrapper> iterator = src.iterator();
        while (iterator.hasNext()) {
            ChannelFutureWrapper channelFutureWrapper = iterator.next();
            if (!channelFutureWrapper.getGroup().equals(group)) {
                iterator.remove();
            }
        }
        if (CommonUtils.isEmptyList(src)) {
            throw new RuntimeException("no provider match for group " + group);
        }
    }
}

