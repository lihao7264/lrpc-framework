package com.atlihao.lrpc.framework.core.common.event.data;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:52 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:52 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class URLChangeWrapper {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 提供者url
     */
    private List<String> providerUrl;

    /**
     * 记录每个ip下的url详细信息（权重，分组等）
     */
    private Map<String,String> nodeDataUrl;
}
