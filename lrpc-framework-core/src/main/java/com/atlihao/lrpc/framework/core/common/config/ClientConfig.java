package com.atlihao.lrpc.framework.core.common.config;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:26 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:26 上午
 * @Version: 1.0.0
 */
@Data
@ToString
public class ClientConfig {

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 注册中心地址
     */
    private String registerAddr;

    /**
     * 代理类型
     * 举例：jdk、javassist
     */
    private String proxyType;

    /**
     * 负载均衡策略
     * 举例：random、newRandom、rotate
     */
    private String routerStrategy;

}
