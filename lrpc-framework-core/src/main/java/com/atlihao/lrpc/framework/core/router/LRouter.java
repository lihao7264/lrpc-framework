package com.atlihao.lrpc.framework.core.router;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.registry.URL;

/**
 * @Description:
 * @Author: 仲阳-李豪
 * @CreateDate: 2023/8/10 9:21 上午
 * @UpdateUser: 仲阳-李豪
 * @UpdateDate: 2023/8/10 9:21 上午
 * @Version: 1.0.0
 */
public interface LRouter {

    /**
     * 刷新路由数组
     *
     * @param selector
     */
    void refreshRouterArr(Selector selector);

    /**
     * 获取请求到连接通道
     *
     * @return
     */
    ChannelFutureWrapper select(Selector selector);

    /**
     * 更新权重信息
     *
     * @param url
     */
    void updateWeight(URL url);

}
