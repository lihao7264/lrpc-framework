package com.atlihao.lrpc.framework.core.registry.zookeeper;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 5:59 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 5:59 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class ProviderNodeInfo {

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 地址
     */
    private String address;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 注册时间
     */
    private String registryTime;

    /**
     * 分组
     */
    private String group;
}
