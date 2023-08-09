package com.atlihao.lrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import com.atlihao.lrpc.framework.core.common.RpcDecoder;
import com.atlihao.lrpc.framework.core.common.RpcEncoder;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.RpcProtocol;
import com.atlihao.lrpc.framework.core.common.config.ClientConfig;
import com.atlihao.lrpc.framework.core.proxy.jdk.JDKProxyFactory;
import com.atlihao.lrpc.framework.interfaces.DataService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:22 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:22 下午
 * @Version: 1.0.0
 */
@Data
@Slf4j
public class Client {

    public static EventLoopGroup clientGroup = new NioEventLoopGroup();

    private ClientConfig clientConfig;


    public RpcReference startClientApplication() throws InterruptedException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 管道中初始化一些逻辑：包含编解码器和客户端响应类
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });
        // 常规的链接netty服务端
        ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddr(), clientConfig.getPort()).sync();
        log.info("============ 服务启动 ============");
        this.startClient(channelFuture);
        // 这里注入一个代理工厂类
        RpcReference rpcReference = new RpcReference(new JDKProxyFactory());
        return rpcReference;
    }


    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setPort(9090);
        clientConfig.setServerAddr("localhost");
        client.setClientConfig(clientConfig);
        RpcReference rpcReference = client.startClientApplication();
        DataService dataService = rpcReference.get(DataService.class);
        for (int i = 0; i < 100; i++) {
            String result = dataService.sendData("test");
            System.out.println(result);
        }
    }

    /**
     * 开启发送线程：专门用于将数据包发送给服务端，起到一个解耦效果
     *
     * @param channelFuture
     */
    private void startClient(ChannelFuture channelFuture) {
        Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
        asyncSendJob.start();
    }

    /**
     * 异步发送信息任务
     */
    class AsyncSendJob implements Runnable {

        private ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞模式
                    RpcInvocation data = SEND_QUEUE.take();
                    // 将RpcInvocation封装到RpcProtocol对象中，再发送给服务端，这里正好对应ServerHandler
                    String json = JSON.toJSONString(data);
                    // 发送请求
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    // Netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
