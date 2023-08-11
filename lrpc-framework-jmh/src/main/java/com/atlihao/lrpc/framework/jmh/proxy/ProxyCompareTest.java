package com.atlihao.lrpc.framework.jmh.proxy;

import com.atlihao.lrpc.framework.core.client.Client;
import com.atlihao.lrpc.framework.core.client.ConnectionHandler;
import com.atlihao.lrpc.framework.core.client.RpcReference;
import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;
import com.atlihao.lrpc.framework.interfaces.DataService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JDK代理 和 Javassist代理性能比较
 */
public class ProxyCompareTest {

    private static Client client;
    private static DataService dataService;
    private static RpcReference rpcReference;

    static {
        client = new Client();
        rpcReference = client.initClientApplication();
        try {
            RpcReferenceWrapper<DataService> rpcReferenceWrapper = new RpcReferenceWrapper<>();
            rpcReferenceWrapper.setTargetClass(DataService.class);
            rpcReferenceWrapper.setGroup("default");
            dataService = rpcReference.get(rpcReferenceWrapper);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        System.out.println("初始化客户端程序");
    }


//    @Benchmark
//    public String testJdkProxy() throws Throwable {
//        String content = dataService.sendData("test");
//        return content;
//    }

    /**
     * 修改lrpc.properties配置文件中的代理模式再重启客户端进行测试
     *
     * @return
     * @throws Throwable
     */
//    @Benchmark
//    public String testJavassistProxy() throws Throwable {
//        String content = dataService.sendData("test");
//        return content;
//    }

    public static void main(String[] args) throws RunnerException {
        // 配置进行2轮热数 测试2轮 1个线程
        // 预热原因：JVM在代码执行多次会有优化
        Options options = new OptionsBuilder().warmupIterations(2).measurementBatchSize(2)
                .forks(1).build();
        new Runner(options).run();
    }
}
