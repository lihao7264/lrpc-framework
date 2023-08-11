package com.atlihao.lrpc.framework.core.proxy.javassist;

import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;
import com.atlihao.lrpc.framework.core.proxy.ProxyFactory;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 12:45 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 12:45 下午
 * @Version: 1.0.0
 */
public class JavassistProxyFactory implements ProxyFactory {
    /**
     * 获取代理实例
     *
     * @param rpcReferenceWrapper
     * @return
     * @throws Throwable
     */
    @Override
    public <T> T getProxy(RpcReferenceWrapper rpcReferenceWrapper) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(), rpcReferenceWrapper.getTargetClass(),
                new JavassistInvocationHandler(rpcReferenceWrapper.getTargetClass()));
    }
}
