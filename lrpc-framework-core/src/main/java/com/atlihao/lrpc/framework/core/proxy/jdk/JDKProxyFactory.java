package com.atlihao.lrpc.framework.core.proxy.jdk;


import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;
import com.atlihao.lrpc.framework.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;


/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 10:00 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 10:00 上午
 * @Version: 1.0.0
 */
public class JDKProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(final RpcReferenceWrapper rpcReferenceWrapper) {
        return (T) Proxy.newProxyInstance(rpcReferenceWrapper.getTargetClass().getClassLoader(), new Class[]{rpcReferenceWrapper.getTargetClass()},
                new JDKClientInvocationHandler(rpcReferenceWrapper));
    }

}
