package com.atlihao.lrpc.framework.core.server;

import com.atlihao.lrpc.framework.core.common.RpcDecoder;
import com.atlihao.lrpc.framework.core.common.RpcEncoder;
import com.atlihao.lrpc.framework.core.common.config.PropertiesBootstrap;
import com.atlihao.lrpc.framework.core.common.config.ServerConfig;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.registry.RegistryService;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.registry.zookeeper.ZookeeperRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Data;

import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;
import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.PROVIDER_URL_SET;

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

    private RegistryService registryService;


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
        this.batchExportUrl();
        bootstrap.bind(serverConfig.getServerPort()).sync();
    }

    public void initServerConfig() {
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.setServerConfig(serverConfig);
    }

    /**
     * 暴露服务信息
     *
     * @param serviceBean
     */
    public void exportService(Object serviceBean) {
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        if (registryService == null) {
            registryService = new ZookeeperRegister(serverConfig.getRegisterAddr());
        }
        //默认选择该对象的第一个实现接口
        Class interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", CommonUtils.getIpAddress());
        url.addParameter("port", String.valueOf(serverConfig.getServerPort()));
        PROVIDER_URL_SET.add(url);
    }

    public void batchExportUrl(){
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (URL url : PROVIDER_URL_SET) {
                    registryService.register(url);
                }
            }
        });
        task.start();
    }


    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        // 初始化服务端配置
        server.initServerConfig();
        // 暴露服务
        server.exportService(new DataServiceImpl());
        // 启动服务端
        server.startApplication();
    }

}
