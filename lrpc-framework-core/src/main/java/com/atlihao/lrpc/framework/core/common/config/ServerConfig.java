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
     * 应用名
     */
    private String applicationName;

}