package com.atlihao.lrpc.framework.core.client;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/13 7:42 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/13 7:42 上午
 * @Version: 1.0.0
 */
@Data
@ToString
public class RpcReferenceFuture<T> {

    private RpcReferenceWrapper rpcReferenceWrapper;

    private Object response;
}