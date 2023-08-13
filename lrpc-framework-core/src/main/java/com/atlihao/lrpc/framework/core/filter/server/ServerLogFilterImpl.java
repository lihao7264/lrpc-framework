package com.atlihao.lrpc.framework.core.filter.server;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.annotations.SPI;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@SPI("before")
public class ServerLogFilterImpl implements LServerFilter {

    @Override
    public void doFilter(RpcInvocation rpcInvocation) {
       log.info(rpcInvocation.getAttachments().get("app_name") + " do invoke -----> " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }

}
