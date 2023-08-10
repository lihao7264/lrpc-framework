package com.atlihao.lrpc.framework.core.registry.zookeeper;

import com.atlihao.lrpc.framework.core.registry.RegistryService;
import com.atlihao.lrpc.framework.core.registry.URL;

import java.util.List;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.PROVIDER_URL_SET;

/**
 * @Description: 注册中心抽象类
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:38 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:38 下午
 * @Version: 1.0.0
 */
public abstract class AbstractRegister implements RegistryService {

    @Override
    public void register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url.getServiceName());
    }

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param url
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 留给子类扩展
     *
     * @param serviceName
     * @return
     */
    public abstract List<String> getProviderIps(String serviceName);


    @Override
    public void doUnSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }
}
