package com.atlihao.lrpc.framework.core.common.event.listener;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.event.LRpcNodeChangeEvent;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.registry.zookeeper.ProviderNodeInfo;

import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.LROUTER;


public class ProviderNodeDataChangeListener implements LRpcListener<LRpcNodeChangeEvent> {

    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            String address = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
            if (address.equals(providerNodeInfo.getAddress())) {
                // 修改权重
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                // 更新权重
                LROUTER.updateWeight(url);
                break;
            }
        }
    }

}
