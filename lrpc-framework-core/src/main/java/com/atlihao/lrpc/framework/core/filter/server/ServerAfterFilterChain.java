package com.atlihao.lrpc.framework.core.filter.server;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 8:13 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 8:13 下午
 * @Version: 1.0.0
 */
public class ServerAfterFilterChain {

    private static List<LServerFilter> lServerFilters = new ArrayList<>();

    public void addServerFilter(LServerFilter lServerFilter) {
        lServerFilters.add(lServerFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (LServerFilter lServerFilter : lServerFilters) {
            lServerFilter.doFilter(rpcInvocation);
        }
    }

}
