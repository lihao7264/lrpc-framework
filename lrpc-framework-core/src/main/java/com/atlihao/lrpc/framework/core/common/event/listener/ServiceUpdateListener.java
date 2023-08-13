package com.atlihao.lrpc.framework.core.common.event.listener;

import com.atlihao.lrpc.framework.core.client.ConnectionHandler;
import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.event.LRpcEvent;
import com.atlihao.lrpc.framework.core.common.event.data.URLChangeWrapper;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.registry.zookeeper.ProviderNodeInfo;
import com.atlihao.lrpc.framework.core.router.Selector;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.LROUTER;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:54 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:54 下午
 * @Version: 1.0.0
 */
@Slf4j
public class ServiceUpdateListener implements LRpcListener<LRpcEvent> {

    @Override
    public void callBack(Object t) {
        // 获取到字节点的数据信息
        URLChangeWrapper urlChangeWrapper = (URLChangeWrapper) t;
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(urlChangeWrapper.getServiceName());
        List<String> matchProviderUrl = urlChangeWrapper.getProviderUrl();
        Set<String> finalUrl = new HashSet<>();
        List<ChannelFutureWrapper> finalChannelFutureWrappers = new ArrayList<>();
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            String oldServerAddress = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
            // 如果老的url没有，则说明已被移除
            if (!matchProviderUrl.contains(oldServerAddress)) {
                continue;
            } else {
                finalChannelFutureWrappers.add(channelFutureWrapper);
                finalUrl.add(oldServerAddress);
            }
        }

        // 此时老的url已被移除，开始检查是否有新的url
        List<ChannelFutureWrapper> newChannelFutureWrapper = new ArrayList<>();
        for (String newProviderUrl : matchProviderUrl) {
            // 新增的节点数据
            if (!finalUrl.contains(newProviderUrl)) {
                ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
                String host = newProviderUrl.split(":")[0];
                Integer port = Integer.valueOf(newProviderUrl.split(":")[1]);
                channelFutureWrapper.setPort(port);
                channelFutureWrapper.setHost(host);
                String urlStr = urlChangeWrapper.getNodeDataUrl().get(newProviderUrl);
                ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(urlStr);
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                channelFutureWrapper.setGroup(providerNodeInfo.getGroup());
                ChannelFuture channelFuture = null;
                try {
                    channelFuture = ConnectionHandler.createChannelFuture(host, port);
                    channelFutureWrapper.setChannelFuture(channelFuture);
                    newChannelFutureWrapper.add(channelFutureWrapper);
                    finalUrl.add(newProviderUrl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        finalChannelFutureWrappers.addAll(newChannelFutureWrapper);
        // 最终在这里更新服务
        CONNECT_MAP.put(urlChangeWrapper.getServiceName(), finalChannelFutureWrappers);
        Selector selector = new Selector();
        selector.setProviderServiceName(urlChangeWrapper.getServiceName());
        LROUTER.refreshRouterArr(selector);
    }

}
