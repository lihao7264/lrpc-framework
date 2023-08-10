package com.atlihao.lrpc.framework.core.common.event;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:51 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:51 下午
 * @Version: 1.0.0
 */
public interface LRpcListener<T> {

    void callBack(Object t);

}
