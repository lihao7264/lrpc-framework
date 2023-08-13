package com.atlihao.lrpc.framework.core.client;

import com.atlihao.lrpc.framework.core.proxy.ProxyFactory;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.CLIENT_CONFIG;

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
     * @param referenceWrapper
     * @param <T>
     * @return
     */
    public <T> T get(RpcReferenceWrapper<T> referenceWrapper) throws Throwable {
        initGlobalRpcReferenceWrapperConfig(referenceWrapper);
        return proxyFactory.getProxy(referenceWrapper);
    }


    /**
     * 初始化远程调用的一些全局配置（比如：超时）
     *
     * @param rpcReferenceWrapper
     */
    private void initGlobalRpcReferenceWrapperConfig(RpcReferenceWrapper rpcReferenceWrapper) {
        if (rpcReferenceWrapper.getTimeout() == null) {
            rpcReferenceWrapper.setTimeOut(CLIENT_CONFIG.getTimeOut());
        }
    }
}
