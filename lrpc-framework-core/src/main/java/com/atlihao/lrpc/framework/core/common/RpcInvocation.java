package com.atlihao.lrpc.framework.core.common;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:23 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:23 上午
 * @Version: 1.0.0
 * 参数举例：
 * {
 * "args": ["test"],
 * "targetMethod": "sendData",
 * "targetServiceName": "com.atlihao.lrpc.framework.interfaces.DataService",
 * "uuid": "861fe7a4-fa0e-4ee4-a72e-01d213370bd5"
 * }
 */
@Data
@ToString
public class RpcInvocation {

    /**
     * 请求的目标方法
     * 举例：sendData
     */
    private String targetMethod;

    /**
     * 请求的目标服务名称
     * 举例：com.atlihao.lrpc.framework.interfaces.DataService
     */
    private String targetServiceName;

    /**
     * 请求参数信息
     */
    private Object[] args;

    /**
     * 请求id
     */
    private String uuid;

    /**
     * 响应：接口响应的数据塞入该字段中（如果是异步调用或者void类型，则为空）
     */
    private Object response;

    /**
     * 异常信息
     */
    private Throwable e;

    /**
     * 重试次数
     */
    private int retry;

    private Map<String,Object> attachments = new HashMap<>();

}
