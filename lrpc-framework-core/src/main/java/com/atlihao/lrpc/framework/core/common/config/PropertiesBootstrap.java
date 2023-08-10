package com.atlihao.lrpc.framework.core.common.config;

import java.io.IOException;

/**
 * @Description:
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
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
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
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStr(PROXY_TYPE));
        return clientConfig;
    }

}
