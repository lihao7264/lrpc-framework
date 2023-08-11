package com.atlihao.lrpc.framework.core.common.config;

import java.io.IOException;

import static com.atlihao.lrpc.framework.core.common.constants.RpcConstants.*;

/**
 * @Description: 配置加载器
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:44 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:44 下午
 * @Version: 1.0.0
 */
public class PropertiesBootstrap {

    private volatile boolean configIsReady;

    public static final String SERVER_PORT = "lrpc.serverPort";
    public static final String REGISTER_ADDRESS = "lrpc.registerAddr";
    public static final String APPLICATION_NAME = "lrpc.applicationName";
    public static final String PROXY_TYPE = "lrpc.proxyType";
    public static final String ROUTER_TYPE = "lrpc.routerStrategy";
    public static final String SERVER_SERIALIZE_TYPE = "lrpc.serverSerialize";
    public static final String CLIENT_SERIALIZE_TYPE = "lrpc.clientSerialize";

    /**
     * 加载服务端配置
     *
     * @return
     */
    public static ServerConfig loadServerConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail,e is {}", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(PropertiesLoader.getPropertiesInteger(SERVER_PORT));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesNotBlank(APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesNotBlank(REGISTER_ADDRESS));
        serverConfig.setServerSerialize(PropertiesLoader.getPropertiesStrDefault(SERVER_SERIALIZE_TYPE, JDK_SERIALIZE_TYPE));
        return serverConfig;
    }

    /**
     * 加载客户端配置
     *
     * @return
     */
    public static ClientConfig loadClientConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadClientConfigFromLocal fail,e is {}", e);
        }
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesNotBlank(APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesNotBlank(REGISTER_ADDRESS));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStrDefault(PROXY_TYPE, JDK_PROXY_TYPE));
        clientConfig.setRouterStrategy(PropertiesLoader.getPropertiesStrDefault(ROUTER_TYPE, RANDOM_ROUTER_TYPE));
        clientConfig.setClientSerialize(PropertiesLoader.getPropertiesStrDefault(SERVER_SERIALIZE_TYPE, JDK_SERIALIZE_TYPE));
        return clientConfig;
    }

}
