package com.atlihao.lrpc.framework.core.filter;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;

/**
 * @Description: 服务端过滤器
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:37 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:37 下午
 * @Version: 1.0.0
 */
public interface LServerFilter extends LFilter {

    /**
     * 执行核心过滤逻辑
     *
     * @param rpcInvocation
     */
    void doFilter(RpcInvocation rpcInvocation);

}
