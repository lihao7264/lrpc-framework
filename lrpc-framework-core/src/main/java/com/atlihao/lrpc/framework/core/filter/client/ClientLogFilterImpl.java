package com.atlihao.lrpc.framework.core.filter.client;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.filter.LClientFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.CLIENT_CONFIG;

/**
 * @Description: 客户端调用日志过滤器
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:39 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:39 下午
 * @Version: 1.0.0
 */
@Slf4j
public class ClientLogFilterImpl implements LClientFilter {
    /**
     * 执行过滤链
     *
     * @param src
     * @param rpcInvocation
     * @return
     */
    @Override
    public void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation) {
        rpcInvocation.getAttachments().put("app_name", CLIENT_CONFIG.getApplicationName());
        log.info(rpcInvocation.getAttachments().get("app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName());
    }
}
