package com.atlihao.lrpc.framework.core.proxy.jdk;


import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;
import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.SEND_QUEUE;
import static com.atlihao.lrpc.framework.core.common.constants.RpcConstants.DEFAULT_TIMEOUT;

/**
 * @Description: 各种代理工厂统一使用这个InvocationHandler
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 10:00 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 10:00 上午
 * @Version: 1.0.0
 */
public class JDKClientInvocationHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private RpcReferenceWrapper rpcReferenceWrapper;

    private Long timeOut = DEFAULT_TIMEOUT;

    public JDKClientInvocationHandler(RpcReferenceWrapper rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
        timeOut = Long.valueOf(String.valueOf(rpcReferenceWrapper.getAttatchments().get("timeOut")));

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 请求信息
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getTargetClass().getName());
        // 这里注入一个uuid，对每一次的请求都做单独区分
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttatchments());
        rpcInvocation.setRetry(rpcReferenceWrapper.getRetry());
        // 请求队列集合：将请求的参数放入到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        // 响应结果
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        long beginTime = System.currentTimeMillis();
        int retryTimes = 0;
        // 请求超时处理：客户端请求超时的一个判断依据
        while (System.currentTimeMillis() - beginTime < timeOut || rpcInvocation.getRetry() > 0) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                RpcInvocation rpcInvocationResp = (RpcInvocation) object;
                // 正常结果
                if (rpcInvocationResp.getRetry() == 0 || (rpcInvocationResp.getRetry() != 0 && rpcInvocationResp.getE() == null)) {
                    RESP_MAP.remove(rpcInvocation.getUuid());
                    return rpcInvocationResp.getResponse();
                } else if (rpcInvocationResp.getE() != null) {
                    // 每次重试后，都将retry值扣减1
                    if (rpcInvocationResp.getRetry() == 0) {
                        RESP_MAP.remove(rpcInvocation.getUuid());
                        throw rpcInvocationResp.getE();
                    }
                    //如果是因为超时的情况，才会触发重试规则，否则重试机制不生效
                    if (System.currentTimeMillis() - beginTime > timeOut) {
                        retryTimes++;
                        // 重新请求
                        rpcInvocation.setResponse(null);
                        // 减少重试次数
                        rpcInvocation.setRetry(rpcInvocationResp.getRetry() - 1);
                        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
                        SEND_QUEUE.add(rpcInvocation);
                    }
                }
            }
        }
        // 应对一些请求超时的情况
        RESP_MAP.remove(rpcInvocation.getUuid());
        throw new TimeoutException("Wait for response from server on client " + timeOut + "ms,retry times is " + retryTimes + ",service's name is " + rpcInvocation.getTargetServiceName() + "#" + rpcInvocation.getTargetMethod());
    }
}
