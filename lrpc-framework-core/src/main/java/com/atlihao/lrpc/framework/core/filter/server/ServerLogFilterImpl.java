package com.atlihao.lrpc.framework.core.filter.server;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 服务端日志过滤器
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 4:45 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 4:45 下午
 * @Version: 1.0.0
 */
public class ServerLogFilterImpl implements LServerFilter {

    private static Logger logger = LoggerFactory.getLogger(ServerLogFilterImpl.class);

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
        System.out.println(rpcInvocation.getAttachments().get("app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }

}
