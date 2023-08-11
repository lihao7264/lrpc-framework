package com.atlihao.lrpc.framework.core.proxy;

import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 12:46 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 12:46 下午
 * @Version: 1.0.0
 */
public interface ProxyFactory {

    /**
     * 获取代理实例
     *
     * @param rpcReferenceWrapper
     * @param <T>
     * @return
     * @throws Throwable
     */
    <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable;

}
