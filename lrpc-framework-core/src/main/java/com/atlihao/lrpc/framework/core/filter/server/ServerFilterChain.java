package com.atlihao.lrpc.framework.core.filter.server;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:44 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:44 下午
 * @Version: 1.0.0
 */
public class ServerFilterChain {

    private static List<LServerFilter> iServerFilters = new ArrayList<>();

    public void addServerFilter(LServerFilter iServerFilter) {
        iServerFilters.add(iServerFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (LServerFilter iServerFilter : iServerFilters) {
            iServerFilter.doFilter(rpcInvocation);
        }
    }
}
