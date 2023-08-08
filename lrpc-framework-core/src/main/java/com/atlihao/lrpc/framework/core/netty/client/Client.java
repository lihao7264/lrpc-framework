package com.atlihao.lrpc.framework.core.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.UnsupportedEncodingException;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/7 9:18 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/7 9:18 下午
 * @Version: 1.0.0
 */
public class Client {

    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        ChannelFuture channelFuture = bootstrap.connect("localhost", 9090).sync();
        while (true) {
            Thread.sleep(2000);
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("send data[end]bbbb".getBytes()));
            System.out.println("发送数据！");
        }
    }
}
