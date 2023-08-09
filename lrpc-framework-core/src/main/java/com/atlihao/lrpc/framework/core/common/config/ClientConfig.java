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
     * 客户端端口
     */
    private Integer port;

    /**
     * 客户端服务地址
     */
    private String serverAddr;

}
