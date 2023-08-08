package com.atlihao.lrpc.framework.core.netty.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/7 9:21 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/7 9:21 下午
 * @Version: 1.0.0
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("[server] 接收到数据请求" + msg.toString());
        ctx.writeAndFlush(Unpooled.copiedBuffer("msg from server".getBytes("UTF-8")));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
