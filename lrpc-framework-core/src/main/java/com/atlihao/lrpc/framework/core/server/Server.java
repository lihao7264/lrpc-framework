package com.atlihao.lrpc.framework.core.server;

import com.atlihao.lrpc.framework.core.common.RpcDecoder;
import com.atlihao.lrpc.framework.core.common.RpcEncoder;
import com.atlihao.lrpc.framework.core.common.ServerServiceSemaphoreWrapper;
import com.atlihao.lrpc.framework.core.common.annotations.SPI;
import com.atlihao.lrpc.framework.core.common.config.PropertiesBootstrap;
import com.atlihao.lrpc.framework.core.common.config.ServerConfig;
import com.atlihao.lrpc.framework.core.common.event.LRpcListenerLoader;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.filter.LServerFilter;
import com.atlihao.lrpc.framework.core.filter.server.ServerAfterFilterChain;
import com.atlihao.lrpc.framework.core.filter.server.ServerBeforeFilterChain;
import com.atlihao.lrpc.framework.core.registry.RegistryService;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.registry.zookeeper.AbstractRegister;
import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import com.atlihao.lrpc.framework.core.service.impl.DataServiceImpl;
import com.atlihao.lrpc.framework.core.service.impl.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.EXTENSION_LOADER;
import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.*;
import static com.atlihao.lrpc.framework.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:18 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:18 下午
 * @Version: 1.0.0
 */
@Slf4j
@Data
public class Server {

    private static EventLoopGroup bossGroup = null;

    private static EventLoopGroup workerGroup = null;

    private ServerConfig serverConfig;

    private static LRpcListenerLoader lRpcListenerLoader;


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
        // 服务端采用单一长连接模式，这里所支持的最大连接数应和机器本身的性能有关
        // 连接防护的handler应绑定在Main-Reactor（boss reactor）上
        bootstrap.handler(new MaxConnectionLimitHandler(serverConfig.getMaxConnections()));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                System.out.println("初始化provider过程");
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(serverConfig.getMaxServerRequestData(), delimiter));
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                // 注意:出现堵塞的情况发生，建议将核心业务内容分配给业务线程池处理
                ch.pipeline().addLast(new ServerHandler());
            }
        });
        this.batchExportUrl();
        // 开始准备接收请求的任务
        SERVER_CHANNEL_DISPATCHER.startDataConsume();
        bootstrap.bind(serverConfig.getServerPort()).sync();
        IS_STARTED = true;
        log.info("[startApplication] server is started!");
    }

    /**
     * 服务端初始化环节的加载策略
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public void initServerConfig() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.setServerConfig(serverConfig);
        SERVER_CONFIG = serverConfig;
        // 初始化线程池和队列的配置
        SERVER_CHANNEL_DISPATCHER.init(SERVER_CONFIG.getServerQueueSize(), SERVER_CONFIG.getServerBizThreadNums());
        SERVER_SERIALIZE_FACTORY = (SerializeFactory) EXTENSION_LOADER.loadExtensionInstance(SerializeFactory.class, serverConfig.getServerSerialize());
        // 过滤链技术初始化
        List<Object> result = EXTENSION_LOADER.loadAllExtensionInstance(LServerFilter.class);
        ServerBeforeFilterChain serverBeforeFilterChain = new ServerBeforeFilterChain();
        ServerAfterFilterChain serverAfterFilterChain = new ServerAfterFilterChain();
        // 过滤器初始化环节新增 前置过滤器和后置过滤器
        for (Object instance : result) {
            Class lServerFilterClass = instance.getClass();
            if (lServerFilterClass == null) {
                throw new RuntimeException("no match lServerFilter type ");
            }
            SPI spi = (SPI) lServerFilterClass.getDeclaredAnnotation(SPI.class);
            if (spi != null && "before".equals(spi.value())) {
                serverBeforeFilterChain.addServerFilter((LServerFilter) instance);
            } else if (spi != null && "after".equals(spi.value())) {
                serverAfterFilterChain.addServerFilter((LServerFilter) instance);
            }
        }
        SERVER_AFTER_FILTER_CHAIN = serverAfterFilterChain;
        SERVER_BEFORE_FILTER_CHAIN = serverBeforeFilterChain;
    }

    /**
     * 暴露服务信息
     *
     * @param serviceWrapper
     */
    public void exportService(ServiceWrapper serviceWrapper) {
        Object serviceBean = serviceWrapper.getServiceBean();
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        if (REGISTRY_SERVICE == null) {
            try {
                REGISTRY_SERVICE = (AbstractRegister) EXTENSION_LOADER.loadExtensionInstance(RegistryService.class, serverConfig.getRegisterType());
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }
        // 默认选择该对象的第一个实现接口
        Class interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", CommonUtils.getIpAddress());
        url.addParameter("port", String.valueOf(serverConfig.getServerPort()));
        url.addParameter("group", String.valueOf(serviceWrapper.getGroup()));
        url.addParameter("limit", String.valueOf(serviceWrapper.getLimit()));
        // 设置服务端的限流器
        SERVER_SERVICE_SEMAPHORE_MAP.put(interfaceClass.getName(), new ServerServiceSemaphoreWrapper(serviceWrapper.getLimit()));
        PROVIDER_URL_SET.add(url);
        if (CommonUtils.isNotEmpty(serviceWrapper.getServiceToken())) {
            PROVIDER_SERVICE_WRAPPER_MAP.put(interfaceClass.getName(), serviceWrapper);
        }
    }

    public void batchExportUrl() {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (URL url : PROVIDER_URL_SET) {
                    REGISTRY_SERVICE.register(url);
                }
            }
        });
        task.start();
    }


    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        Server server = new Server();
        // 初始化服务端配置
        server.initServerConfig();
        lRpcListenerLoader = new LRpcListenerLoader();
        lRpcListenerLoader.init();
        // 暴露服务
        ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(new DataServiceImpl(), "dev");
        dataServiceServiceWrapper.setServiceToken("token-a");
        dataServiceServiceWrapper.setLimit(2);
        ServiceWrapper userServiceServiceWrapper = new ServiceWrapper(new UserServiceImpl(), "dev");
        userServiceServiceWrapper.setServiceToken("token-b");
        userServiceServiceWrapper.setLimit(2);
        server.exportService(dataServiceServiceWrapper);
        server.exportService(userServiceServiceWrapper);
        // 注册一个shutdownHook的钩子，当jvm进程关闭时触发
        ApplicationShutdownHook.registryShutdownHook();
        // 启动服务端
        server.startApplication();
    }

}
