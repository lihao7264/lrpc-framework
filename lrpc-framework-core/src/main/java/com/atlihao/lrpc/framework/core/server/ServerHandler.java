package com.atlihao.lrpc.framework.core.server;

import com.atlihao.lrpc.framework.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;

import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.SERVER_CHANNEL_DISPATCHER;

/**
 * @Description: 服务端接收数据后的处理器
 * 非共享模式，不存在线程安全问题
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:15 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:15 下午
 * @Version: 1.0.0
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvocationTargetException, IllegalAccessException {
        ServerChannelReadData serverChannelReadData = new ServerChannelReadData();
        serverChannelReadData.setChannelHandlerContext(ctx);
        // 服务端接收数据时，统一以RpcProtocol协议的格式接收
        serverChannelReadData.setRpcProtocol((RpcProtocol) msg);
        // 放入channel分发器
        SERVER_CHANNEL_DISPATCHER.add(serverChannelReadData);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 统一做异常捕获
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }

}
