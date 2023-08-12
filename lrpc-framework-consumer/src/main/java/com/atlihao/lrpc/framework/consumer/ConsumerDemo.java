package com.atlihao.lrpc.framework.consumer;

import com.atlihao.lrpc.framework.core.client.Client;
import com.atlihao.lrpc.framework.core.client.ConnectionHandler;
import com.atlihao.lrpc.framework.core.client.RpcReference;
import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;
import com.atlihao.lrpc.framework.interfaces.DataService;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

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
        rpcReferenceWrapper.setTimeOut(3000L);
        // 在初始化之前必须要设置对应的上下文
        DataService dataService = rpcReference.get(rpcReferenceWrapper);
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 10000; i++) {
            try {
                FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return dataService.sendData("test");
                    }
                });
                executorService.submit(futureTask);
                List<String> resultList = dataService.getList();
                System.out.println("result List is :" + resultList);
                System.out.println("等待一段时间后");
                String result = futureTask.get();
                System.out.println(result);
            } catch (Exception e) {
                System.out.println(i);
                e.printStackTrace();
            }
        }
        System.out.println("结束调用60000次");
    }
}
