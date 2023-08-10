package com.atlihao.lrpc.framework.core.registry;

/**
 * @Description: 注册中心服务
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:37 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:37 下午
 * @Version: 1.0.0
 */
public interface RegistryService {


    /**
     * 注册url：将lrpc服务写入注册中心节点
     * 当出现网络抖动时，需进行适当的重试做法
     * 注册服务url时，需写入持久化文件中
     *
     * @param url
     */
    void register(URL url);

    /**
     * 服务下线
     * 持久化节点是无法进行服务下线操作的
     * 下线的服务必须保证url是完整匹配的
     * 移除持久化文件中的一些内容信息
     *
     * @param url
     */
    void unRegister(URL url);

    /**
     * 消费方订阅服务
     *
     * @param url
     */
    void subscribe(URL url);


    /**
     * 执行取消订阅内部的逻辑
     *
     * @param url
     */
    void doUnSubscribe(URL url);
}
