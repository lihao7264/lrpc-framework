package com.atlihao.lrpc.framework.consumer.spi.filter;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.filter.LClientFilter;

import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 8:01 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 8:01 下午
 * @Version: 1.0.0
 */
public class LogFilterImpl implements LClientFilter {
    /**
     * 执行过滤链
     *
     * @param src
     * @param rpcInvocation
     * @return
     */
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        System.out.println("test LogFilterImpl");
    }
}
