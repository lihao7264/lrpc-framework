package com.atlihao.lrpc.framework.provider;

import com.atlihao.lrpc.framework.core.common.event.LRpcListenerLoader;
import com.atlihao.lrpc.framework.core.server.*;
import com.atlihao.lrpc.framework.core.service.impl.DataServiceImpl;
import com.atlihao.lrpc.framework.core.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 8:06 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 8:06 下午
 * @Version: 1.0.0
 */
public class ProviderDemo {

    public static void main(String[] args) throws InterruptedException, ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        Server server = new Server();
        server.initServerConfig();
        LRpcListenerLoader lRpcListenerLoader = new LRpcListenerLoader();
        lRpcListenerLoader.init();
        ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(new DataServiceImpl(), "dev");
        dataServiceServiceWrapper.setServiceToken("token-a");
        dataServiceServiceWrapper.setLimit(2);
        ServiceWrapper userServiceServiceWrapper = new ServiceWrapper(new UserServiceImpl(), "dev");
        userServiceServiceWrapper.setServiceToken("token-b");
        userServiceServiceWrapper.setLimit(2);
        server.exportService(dataServiceServiceWrapper);
        server.exportService(userServiceServiceWrapper);
        ApplicationShutdownHook.registryShutdownHook();
        server.startApplication();
    }

}
