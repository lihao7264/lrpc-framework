package com.atlihao.lrpc.framework.core.common.cache;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.config.ClientConfig;
import com.atlihao.lrpc.framework.core.registry.URL;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 客户端公用缓存 存储请求队列等公共信息
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

    /**
     * 客户端配置
     */
    public static ClientConfig CLIENT_CONFIG;

    /**
     * provider名称 --> 该服务有哪些集群URL
     */
    public static List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    /**
     *
     */
    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();

    /**
     * 服务地址
     */
    public static Set<String> SERVER_ADDRESS = new HashSet<>();

    /**
     * 每次进行远程调用时，都从这里面去选择服务提供者
     */
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();

}
