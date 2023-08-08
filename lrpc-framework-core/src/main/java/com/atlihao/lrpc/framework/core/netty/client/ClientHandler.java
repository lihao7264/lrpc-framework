package com.atlihao.lrpc.framework.core.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/7 9:19 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/7 9:19 下午
 * @Version: 1.0.0
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String response = new String(byteBuf.array());
        System.out.printf("[client] 接收到信息: %s \n", response);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
