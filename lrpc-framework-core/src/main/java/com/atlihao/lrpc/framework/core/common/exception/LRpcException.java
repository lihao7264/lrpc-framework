package com.atlihao.lrpc.framework.core.common.exception;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 8:19 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 8:19 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class LRpcException extends RuntimeException {

    private RpcInvocation rpcInvocation;

    public LRpcException(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }


}
