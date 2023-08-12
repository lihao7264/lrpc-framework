package com.atlihao.lrpc.framework.core.common.cache;

import com.atlihao.lrpc.framework.core.common.ChannelFuturePollingRef;
import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.config.ClientConfig;
import com.atlihao.lrpc.framework.core.filter.client.ClientFilterChain;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.registry.zookeeper.AbstractRegister;
import com.atlihao.lrpc.framework.core.router.LRouter;
import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import com.atlihao.lrpc.framework.core.spi.ExtensionLoader;

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
     * provider名称 --> 该服务有哪些集群URL
     */
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    /**
     * com.atlihao.lrpc.test.service -> <<ip:host,urlString>,<ip:host,urlString>,<ip:host,urlString>>
     */
    public static Map<String, Map<String, String>> URL_MAP = new ConcurrentHashMap<>();

    /**
     * 服务地址
     */
    public static Set<String> SERVER_ADDRESS = new HashSet<>();

    /**
     * 每次进行远程调用时，都从这里面去选择服务提供者
     */
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();

    /**
     * 随机请求的Map
     */
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new ConcurrentHashMap<>();

    /**
     * 轮训对象
     */
    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();


    public static LRouter LROUTER;

    /**
     * 客户端序列化工厂
     */
    public static SerializeFactory CLIENT_SERIALIZE_FACTORY;

    /**
     * 客户端配置
     */
    public static ClientConfig CLIENT_CONFIG;

    /**
     * 客户端filter链路
     */
    public static ClientFilterChain CLIENT_FILTER_CHAIN;

    /**
     * 抽象注册中心
     */
    public static AbstractRegister ABSTRACT_REGISTER;

    /**
     * spi
     */
    public static ExtensionLoader EXTENSION_LOADER = new ExtensionLoader();


    /*************** 随机策略方式二 ****************/
    public static Map<String, Integer> SERVICE_ROUTER_TOTAL_WEIGHT_MAP = new ConcurrentHashMap<>();

}
