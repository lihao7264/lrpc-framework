package com.atlihao.lrpc.framework.core.common.event.listener;

import com.atlihao.lrpc.framework.core.common.event.LRpcDestroyEvent;
import com.atlihao.lrpc.framework.core.registry.URL;

import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.PROVIDER_URL_SET;
import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.REGISTRY_SERVICE;

/**
 * 服务注销 监听器
 */
public class ServiceDestroyListener implements LRpcListener<LRpcDestroyEvent> {

    @Override
    public void callBack(Object t) {
        for (URL url : PROVIDER_URL_SET) {
            REGISTRY_SERVICE.unRegister(url);
        }
    }
}
