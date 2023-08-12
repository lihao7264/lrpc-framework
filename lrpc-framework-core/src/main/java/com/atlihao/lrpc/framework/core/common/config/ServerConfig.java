package com.atlihao.lrpc.framework.core.common.config;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:27 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:27 上午
 * @Version: 1.0.0
 */
@Data
@ToString
public class ServerConfig {

    /**
     * 服务端口
     */
    private Integer serverPort;

    /**
     * 注册中心地址
     */
    private String registerAddr;

    /**
     * 注册中心类型
     */
    private String registerType;

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 服务端序列化方式
     * 举例：hession2、kryo、jdk、fastjson
     */
    private String serverSerialize;

}
