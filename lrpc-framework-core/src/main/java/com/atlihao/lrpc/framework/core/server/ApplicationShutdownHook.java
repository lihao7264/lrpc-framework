package com.atlihao.lrpc.framework.core.server;

import com.atlihao.lrpc.framework.core.common.event.LRpcDestroyEvent;
import com.atlihao.lrpc.framework.core.common.event.LRpcListenerLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.IdentityHashMap;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 6:17 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 6:17 下午
 * @Version: 1.0.0
 */
@Slf4j
public class ApplicationShutdownHook {

    /**
     * 注册一个shutdownHook的钩子，当jvm进程关闭时触发
     */
    public static void registryShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("[registryShutdownHook] ==== ");
                LRpcListenerLoader.sendSyncEvent(new LRpcDestroyEvent("destroy"));
            }
        }));
    }
}
