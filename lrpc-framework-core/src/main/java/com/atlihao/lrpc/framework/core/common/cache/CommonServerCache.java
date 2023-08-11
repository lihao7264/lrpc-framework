package com.atlihao.lrpc.framework.core.common.cache;

import com.atlihao.lrpc.framework.core.common.config.ServerConfig;
import com.atlihao.lrpc.framework.core.filter.server.ServerFilterChain;
import com.atlihao.lrpc.framework.core.registry.RegistryService;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import com.atlihao.lrpc.framework.core.server.ServiceWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 服务端缓存 存储提供者等公共信息
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:26 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:26 上午
 * @Version: 1.0.0
 */
public class CommonServerCache {

    /**
     * <服务接口,服务实例>：com.atlihao.lrpc.framework.interfaces.DataService -> DataServiceImpl@1770
     */
    public static final Map<String, Object> PROVIDER_CLASS_MAP = new HashMap<>();


    /**
     * 提供者URL
     */
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();


    /**
     * 注册服务
     */
    public static RegistryService REGISTRY_SERVICE;

    /**
     * 服务端序列化工厂
     */
    public static SerializeFactory SERVER_SERIALIZE_FACTORY;

    /**
     * 服务端配置
     */
    public static ServerConfig SERVER_CONFIG;

    /**
     * 服务端过滤链
     */
    public static ServerFilterChain SERVER_FILTER_CHAIN;


    public static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap<>();

}
