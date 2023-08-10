package com.atlihao.lrpc.framework.core.common.event;

import com.atlihao.lrpc.framework.core.common.event.listener.LRpcListener;
import com.atlihao.lrpc.framework.core.common.event.listener.ProviderNodeDataChangeListener;
import com.atlihao.lrpc.framework.core.common.event.listener.ServiceDestroyListener;
import com.atlihao.lrpc.framework.core.common.event.listener.ServiceUpdateListener;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:51 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:51 下午
 * @Version: 1.0.0
 */
public class LRpcListenerLoader {

    private static List<LRpcListener> lRpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(LRpcListener lRpcListener) {
        lRpcListenerList.add(lRpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
        registerListener(new ServiceDestroyListener());
        registerListener(new ProviderNodeDataChangeListener());
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o 接口
     */
    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    /**
     * 同步事件处理，可能会堵塞
     *
     * @param lRpcEvent
     */
    public static void sendSyncEvent(LRpcEvent lRpcEvent) {
        System.out.println(lRpcListenerList);
        if (CommonUtils.isEmptyList(lRpcListenerList)) {
            return;
        }
        for (LRpcListener<?> lRpcListener : lRpcListenerList) {
            Class<?> type = getInterfaceT(lRpcListener);
            if (lRpcListener.getClass().equals(type)) {
                try {
                    lRpcListener.callBack(lRpcEvent.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendEvent(LRpcEvent lRpcEvent) {
        if (CommonUtils.isEmptyList(lRpcListenerList)) {
            return;
        }
        for (LRpcListener<?> lRpcListener : lRpcListenerList) {
            Class<?> type = getInterfaceT(lRpcListener);
            if (lRpcEvent.getClass().equals(type)) {
                eventThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            lRpcListener.callBack(lRpcEvent.getData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

}
