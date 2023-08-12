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
import com.atlihao.lrpc.framework.core.filter.LClientFilter;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;
import com.atlihao.lrpc.framework.core.filter.client.ClientFilterChain;
import com.atlihao.lrpc.framework.core.filter.client.ClientLogFilterImpl;
import com.atlihao.lrpc.framework.core.filter.client.DirectInvokeFilterImpl;
import com.atlihao.lrpc.framework.core.filter.client.GroupFilterImpl;
import com.atlihao.lrpc.framework.core.filter.server.ServerFilterChain;
import com.atlihao.lrpc.framework.core.proxy.ProxyFactory;
import com.atlihao.lrpc.framework.core.proxy.javassist.JavassistProxyFactory;
import com.atlihao.lrpc.framework.core.proxy.jdk.JDKProxyFactory;
import com.atlihao.lrpc.framework.core.registry.RegistryService;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.registry.zookeeper.AbstractRegister;
import com.atlihao.lrpc.framework.core.registry.zookeeper.ZookeeperRegister;
import com.atlihao.lrpc.framework.core.router.LRouter;
import com.atlihao.lrpc.framework.core.router.RandomLRouterImpl;
import com.atlihao.lrpc.framework.core.router.RotateLRouterImpl;
import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.fastjson.FastJsonSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.hessian.HessianSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.jdk.JdkSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.kryo.KryoSerializeFactory;
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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.*;
import static com.atlihao.lrpc.framework.core.common.constants.RpcConstants.*;
import static com.atlihao.lrpc.framework.core.spi.ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE;

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

    private LRpcListenerLoader lRpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();


    public RpcReference initClientApplication() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
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
        CLIENT_CONFIG = this.clientConfig;
        // spi扩展的加载部分
        this.initClientConfig();
        // 构建代理类型
        ProxyFactory proxyFactory = (ProxyFactory) EXTENSION_LOADER.loadExtensionInstance(ProxyFactory.class, clientConfig.getProxyType());
        return new RpcReference(proxyFactory);
    }


    /**
     * 启动服务前，需预先订阅对应的rpc服务
     *
     * @param serviceBean
     */
    public void doSubscribeService(Class serviceBean) {
        if (ABSTRACT_REGISTER == null) {
            try {
                ABSTRACT_REGISTER = (AbstractRegister) EXTENSION_LOADER.loadExtensionInstance(RegistryService.class,
                        clientConfig.getRegisterType());
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        Map<String, String> result = ABSTRACT_REGISTER.getServiceWeightMap(serviceBean.getName());
        // 连接map
        URL_MAP.put(serviceBean.getName(), result);
        ABSTRACT_REGISTER.subscribe(url);
    }


    /**
     * 开始和各个provider建立连接 并 监听各个providerNode节点的变化（child变化和nodeData的变化）
     */
    public void doConnectServer() {
        for (URL providerURL : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = ABSTRACT_REGISTER.getProviderIps(providerURL.getServiceName());
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerURL.getServiceName(), providerIp);
                } catch (InterruptedException e) {
                    log.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.addParameter("servicePath", providerURL.getServiceName() + "/provider");
            url.addParameter("providerIps", JSON.toJSONString(providerIps));
            // 添加监听
            ABSTRACT_REGISTER.doAfterSubscribe(url);
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
                    RpcInvocation rpcInvocation = SEND_QUEUE.take();
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(rpcInvocation);
                    if (channelFuture != null) {
                        // 将RpcInvocation封装到RpcProtocol对象中，再发送给服务端，这里正好对应ServerHandler
                        RpcProtocol rpcProtocol = new RpcProtocol(CLIENT_SERIALIZE_FACTORY.serialize(rpcInvocation));
                        // Netty的通道负责发送数据给服务端
                        channelFuture.channel().writeAndFlush(rpcProtocol);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * spi扩展的加载部分:客户端初始化环节的加载策略
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void initClientConfig() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // 初始化路由策略
        LROUTER = (LRouter) EXTENSION_LOADER.loadExtensionInstance(LRouter.class, clientConfig.getRouterStrategy());
        // 初始化序列化框架
        CLIENT_SERIALIZE_FACTORY = (SerializeFactory) EXTENSION_LOADER.loadExtensionInstance(SerializeFactory.class, clientConfig.getClientSerialize());
        // 初始化过滤链 指定过滤的顺序
        List<Object> result = EXTENSION_LOADER.loadAllExtensionInstance(LClientFilter.class);
        ClientFilterChain clientFilterChain = new ClientFilterChain();
        for (Object instance : result) {
            clientFilterChain.addClientFilter((LClientFilter) instance);
        }
        CLIENT_FILTER_CHAIN = clientFilterChain;
    }

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        // 初始化客户端应用
        RpcReference rpcReference = client.initClientApplication();
        // 初始化客户端配置
        client.initClientConfig();
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setTargetClass(DataService.class);
        rpcReferenceWrapper.setGroup("dev");
        rpcReferenceWrapper.setServiceToken("token-a");
        //        rpcReferenceWrapper.setUrl("192.168.43.227:9093");

        // 获取服务的代理对象
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
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
