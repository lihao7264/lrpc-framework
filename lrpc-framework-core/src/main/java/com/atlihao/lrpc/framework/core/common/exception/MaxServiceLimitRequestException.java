package com.atlihao.lrpc.framework.core.common.exception;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 8:20 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 8:20 下午
 * @Version: 1.0.0
 */
public class MaxServiceLimitRequestException extends LRpcException {

    public MaxServiceLimitRequestException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }

}
