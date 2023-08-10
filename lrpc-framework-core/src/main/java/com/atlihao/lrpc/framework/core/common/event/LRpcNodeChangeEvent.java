package com.atlihao.lrpc.framework.core.common.event;

import lombok.AllArgsConstructor;

/**
 * @Description:
 * @Author: 仲阳-李豪
 * @CreateDate: 2023/8/10 11:27 上午
 * @UpdateUser: 仲阳-李豪
 * @UpdateDate: 2023/8/10 11:27 上午
 * @Version: 1.0.0
 */
@AllArgsConstructor
public class LRpcNodeChangeEvent implements LRpcEvent {

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
