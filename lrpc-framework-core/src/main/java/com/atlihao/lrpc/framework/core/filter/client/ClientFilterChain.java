package com.atlihao.lrpc.framework.core.filter.client;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.filter.LClientFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:37 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:37 下午
 * @Version: 1.0.0
 */
public class ClientFilterChain {

    private static List<LClientFilter> lClientFilterList = new ArrayList<>();

    public void addClientFilter(LClientFilter lClientFilter) {
        lClientFilterList.add(lClientFilter);
    }

    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        for (LClientFilter lClientFilter : lClientFilterList) {
            lClientFilter.doFilter(src, rpcInvocation);
        }
    }

}
