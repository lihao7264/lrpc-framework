package com.atlihao.lrpc.framework.core.server;

import com.atlihao.lrpc.framework.core.common.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 3:34 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 3:34 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class ServerChannelReadData {

    private RpcProtocol rpcProtocol;

    private ChannelHandlerContext channelHandlerContext;
}
