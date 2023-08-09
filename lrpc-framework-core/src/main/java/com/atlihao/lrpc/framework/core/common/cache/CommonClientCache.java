package com.atlihao.lrpc.framework.core.common.cache;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 公用缓存 存储请求队列等公共信息
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:18 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:18 上午
 * @Version: 1.0.0
 */
public class CommonClientCache {

    /**
     * 发送队列
     */
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue<>(100);

    /**
     * 响应结果
     */
    public static Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();
}
