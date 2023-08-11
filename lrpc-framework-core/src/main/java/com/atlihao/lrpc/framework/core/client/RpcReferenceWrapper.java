package com.atlihao.lrpc.framework.core.client;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 8:18 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 8:18 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class RpcReferenceWrapper<T> {

    private Class<T> targetClass;

    private String group;
}
