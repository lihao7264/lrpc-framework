package com.atlihao.lrpc.framework.core.dispatcher;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.RpcProtocol;
import com.atlihao.lrpc.framework.core.common.exception.LRpcException;
import com.atlihao.lrpc.framework.core.server.NamedThreadFactory;
import com.atlihao.lrpc.framework.core.server.ServerChannelReadData;

import java.lang.reflect.Method;
import java.util.concurrent.*;

import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.*;

/**
 * @Description: 请求分发器
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 3:33 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 3:33 下午
 * @Version: 1.0.0
 */
public class ServerChannelDispatcher {

    /**
     * RPC 数据队列
     */
    private BlockingQueue<ServerChannelReadData> RPC_DATA_QUEUE;

    private ExecutorService executorService;


    public void init(int queueSize, int bizThreadNums) {
        RPC_DATA_QUEUE = new ArrayBlockingQueue<>(queueSize);
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(), new NamedThreadFactory("lrpc", true));
    }

    public void add(ServerChannelReadData serverChannelReadData) {
        // 加入更细粒度的限流策略
        RPC_DATA_QUEUE.add(serverChannelReadData);
    }

    class ServerJobCoreHandle implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    ServerChannelReadData serverChannelReadData = RPC_DATA_QUEUE.take();
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RpcProtocol rpcProtocol = serverChannelReadData.getRpcProtocol();
                                RpcInvocation rpcInvocation = SERVER_SERIALIZE_FACTORY.deserialize(rpcProtocol.getContent(), RpcInvocation.class);
                                // 执行前置过滤器链路
                                try {
                                    SERVER_BEFORE_FILTER_CHAIN.doFilter(rpcInvocation);
                                } catch (Exception cause) {
                                    // 针对自定义异常进行捕获 且 直接返回异常信息给到客户端，然后打印结果
                                    if (cause instanceof LRpcException) {
                                        LRpcException rpcException = (LRpcException) cause;
                                        RpcInvocation reqParam = rpcException.getRpcInvocation();
                                        rpcInvocation.setE(rpcException);
                                        byte[] body = SERVER_SERIALIZE_FACTORY.serialize(reqParam);
                                        RpcProtocol respRpcProtocol = new RpcProtocol(body);
                                        serverChannelReadData.getChannelHandlerContext().writeAndFlush(respRpcProtocol);
                                        return;
                                    }
                                }
                                // PROVIDER_CLASS_MAP：在启动时，预先存储的Bean集合
                                Object targetObject = PROVIDER_CLASS_MAP.get(rpcInvocation.getTargetServiceName());
                                // 当前类的方法列表
                                Method[] methods = targetObject.getClass().getDeclaredMethods();
                                Object result = null;
                                // 业务函数实际执行位置
                                for (Method method : methods) {
                                    // 遍历获取方法列表
                                    // 通过反射找到目标对象，再执行目标方法并返回对应值
                                    if (method.getName().equals(rpcInvocation.getTargetMethod())) {
                                        try {
                                            if (method.getReturnType().equals(Void.TYPE)) {
                                                method.invoke(targetObject, rpcInvocation.getArgs());

                                            } else {
                                                result = method.invoke(targetObject, rpcInvocation.getArgs());
                                            }
                                        } catch (Exception e) {
                                            // 业务异常
                                            rpcInvocation.setE(e);
                                        }
                                        break;
                                    }
                                }
                                // 设置返回结果
                                rpcInvocation.setResponse(result);
                                // 执行后置过滤器链
                                SERVER_AFTER_FILTER_CHAIN.doFilter(rpcInvocation);
                                RpcProtocol respRpcProtocol = new RpcProtocol(SERVER_SERIALIZE_FACTORY.serialize(rpcInvocation));
                                serverChannelReadData.getChannelHandlerContext().writeAndFlush(respRpcProtocol);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startDataConsume() {
        Thread thread = new Thread(new ServerJobCoreHandle());
        thread.start();
    }
}
