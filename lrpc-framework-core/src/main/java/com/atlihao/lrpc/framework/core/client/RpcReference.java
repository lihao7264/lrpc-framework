package com.atlihao.lrpc.framework.core.client;

import com.atlihao.lrpc.framework.core.proxy.ProxyFactory;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:20 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:20 下午
 * @Version: 1.0.0
 */
public class RpcReference {

    public ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> tClass) throws Throwable {
        return proxyFactory.getProxy(tClass);
    }
}
