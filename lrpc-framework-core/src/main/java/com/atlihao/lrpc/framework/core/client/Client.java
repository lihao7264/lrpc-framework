package com.atlihao.lrpc.framework.core.client;

import com.alibaba.fastjson.JSON;
import com.atlihao.lrpc.framework.core.common.RpcDecoder;
import com.atlihao.lrpc.framework.core.common.RpcEncoder;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.RpcProtocol;
import com.atlihao.lrpc.framework.core.common.config.ClientConfig;
import com.atlihao.lrpc.framework.core.common.config.PropertiesBootstrap;
import com.atlihao.lrpc.framework.core.common.event.LRpcListenerLoader;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.proxy.javassist.JavassistProxyFactory;
import com.atlihao.lrpc.framework.core.proxy.jdk.JDKProxyFactory;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.registry.zookeeper.AbstractRegister;
import com.atlihao.lrpc.framework.core.registry.zookeeper.ZookeeperRegister;
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

import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;
import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;

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

    private AbstractRegister abstractRegister;

    private LRpcListenerLoader lRpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();


    public RpcReference initClientApplication() {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
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
        // 初始化监听器加载器
        lRpcListenerLoader = new LRpcListenerLoader();
        lRpcListenerLoader.init();
        // 加载客户端配置
        this.clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        // 枸酱代理类型
        RpcReference rpcReference;
        if ("javassist".equals(clientConfig.getProxyType())) {
            rpcReference = new RpcReference(new JavassistProxyFactory());
        } else {
            rpcReference = new RpcReference(new JDKProxyFactory());
        }
        return rpcReference;
    }


    /**
     * 启动服务前，需预先订阅对应的rpc服务
     *
     * @param serviceBean
     */
    public void doSubscribeService(Class serviceBean) {
        if (abstractRegister == null) {
            abstractRegister = new ZookeeperRegister(clientConfig.getRegisterAddr());
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        abstractRegister.subscribe(url);
    }


    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (String providerServiceName : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerServiceName);
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerServiceName, providerIp);
                } catch (InterruptedException e) {
                    log.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.setServiceName(providerServiceName);
            // 添加监听
            abstractRegister.doAfterSubscribe(url);
        }
    }


    /**
     * 开启发送线程：专门用于将数据包发送给服务端，起到一个解耦效果
     */
    public void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }

    /**
     * 异步发送信息任务
     */
    class AsyncSendJob implements Runnable {

        public AsyncSendJob() {

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
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    // Netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        // 初始化客户端应用
        RpcReference rpcReference = client.initClientApplication();
        // 获取服务的代理对象
        DataService dataService = rpcReference.get(DataService.class);
        // 订阅某个服务
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        // 连接服务端
        client.doConnectServer();
        // 启动客户端
        client.startClient();
        for (int i = 0; i < 100; i++) {
            try {
                String result = dataService.sendData("test");
                System.out.println(result);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}