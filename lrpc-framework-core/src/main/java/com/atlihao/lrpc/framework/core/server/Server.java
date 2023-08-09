package com.atlihao.lrpc.framework.core.server;

import com.atlihao.lrpc.framework.core.common.RpcDecoder;
import com.atlihao.lrpc.framework.core.common.RpcEncoder;
import com.atlihao.lrpc.framework.core.common.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:18 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:18 下午
 * @Version: 1.0.0
 */
@Data
public class Server {

    private static EventLoopGroup bossGroup = null;

    private static EventLoopGroup workerGroup = null;

    private ServerConfig serverConfig;


    public void startApplication() throws InterruptedException {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                System.out.println("初始化provider过程");
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        bootstrap.bind(serverConfig.getPort()).sync();
    }

    public void registryService(Object serviceBean) {
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        Class interfaceClass = classes[0];
        // <提供者class,提供者的实例>
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(9090);
        server.setServerConfig(serverConfig);
        server.registryService(new DataServiceImpl());
        server.startApplication();
    }
}
