package com.atlihao.lrpc.framework.core.filter;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;

import java.util.List;

/**
 * @Description: 客户端过滤器
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:37 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:37 下午
 * @Version: 1.0.0
 */
public interface LClientFilter extends LFilter {

    /**
     * 执行过滤链
     *
     * @param src
     * @param rpcInvocation
     * @return
     */
    void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation);
}
