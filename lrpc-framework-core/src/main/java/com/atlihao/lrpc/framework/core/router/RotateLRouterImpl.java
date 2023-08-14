package com.atlihao.lrpc.framework.core.router;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.registry.URL;

import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.*;

/**
 * @Description: 轮训路由策略
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 9:28 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 9:28 上午
 * @Version: 1.0.0
 */
public class RotateLRouterImpl implements LRouter {


    /**
     * 刷新路由数组
     *
     * @param selector
     */
    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for (int i = 0; i < channelFutureWrappers.size(); i++) {
            arr[i] = channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
    }

    /**
     * 获取请求到连接通道
     *
     * @param selector
     * @return
     */
    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getChannelFutureWrappers());
    }

    /**
     * 更新权重信息
     *
     * @param url
     */
    @Override
    public void updateWeight(URL url) {

    }
}
