package com.atlihao.lrpc.framework.core.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 5:54 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 5:54 下午
 * @Version: 1.0.0
 */
@AllArgsConstructor
public class LRpcUpdateEvent implements LRpcEvent {

    private Object data;

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public LRpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}