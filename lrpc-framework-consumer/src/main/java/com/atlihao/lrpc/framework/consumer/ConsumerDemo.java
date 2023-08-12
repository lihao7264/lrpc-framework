package com.atlihao.lrpc.framework.consumer;

import com.atlihao.lrpc.framework.core.client.Client;
import com.atlihao.lrpc.framework.core.client.ConnectionHandler;
import com.atlihao.lrpc.framework.core.client.RpcReference;
import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;
import com.atlihao.lrpc.framework.interfaces.DataService;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 7:59 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 7:59 下午
 * @Version: 1.0.0
 */
public class ConsumerDemo {

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
        rpcReferenceWrapper.setTargetClass(DataService.class);
        rpcReferenceWrapper.setGroup("dev");
        rpcReferenceWrapper.setServiceToken("token-a");
        // 在初始化之前必须要设置对应的上下文
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        for (int i = 0; i < 10000; i++) {
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
