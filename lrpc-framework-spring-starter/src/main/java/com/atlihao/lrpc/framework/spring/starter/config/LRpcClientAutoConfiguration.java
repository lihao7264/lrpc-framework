package com.atlihao.lrpc.framework.spring.starter.config;

import com.atlihao.lrpc.framework.core.client.Client;
import com.atlihao.lrpc.framework.core.client.ConnectionHandler;
import com.atlihao.lrpc.framework.core.client.RpcReference;
import com.atlihao.lrpc.framework.core.client.RpcReferenceWrapper;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcReference;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/13 5:14 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/13 5:14 下午
 * @Version: 1.0.0
 */
@Slf4j
public class LRpcClientAutoConfiguration implements BeanPostProcessor, ApplicationListener<ApplicationReadyEvent> {

    private static RpcReference rpcReference = null;
    private static Client client = null;
    private volatile boolean needInitClient = false;
    private volatile boolean hasInitClientConfig = false;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (needInitClient && client != null) {
            log.info(" ================== [{}] started success ================== ", client.getClientConfig().getApplicationName());
            ConnectionHandler.setBootstrap(client.getBootstrap());
            client.doConnectServer();
            client.startClient();
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(LRpcReference.class)) {
                if (!hasInitClientConfig) {
                    client = new Client();
                    try {
                        rpcReference = client.initClientApplication();
                    } catch (Exception e) {
                        log.error("[LRpcClientAutoConfiguration] postProcessAfterInitialization has error ", e);
                        throw new RuntimeException(e);
                    }
                    hasInitClientConfig = true;
                }
            }
            needInitClient = true;
            LRpcReference lRpcReference = field.getAnnotation(LRpcReference.class);
            if (Objects.isNull(lRpcReference)) {
                continue;
            }
            try {
                field.setAccessible(true);
                Object refObj = field.get(bean);
                RpcReferenceWrapper rpcReferenceWrapper = new RpcReferenceWrapper();
                rpcReferenceWrapper.setTargetClass(field.getType());
                rpcReferenceWrapper.setGroup(lRpcReference.group());
                rpcReferenceWrapper.setServiceToken(lRpcReference.serviceToken());
                rpcReferenceWrapper.setUrl(lRpcReference.url());
                rpcReferenceWrapper.setTimeOut(lRpcReference.timeOut());
                // 失败重试次数
                rpcReferenceWrapper.setRetry(lRpcReference.retry());
                rpcReferenceWrapper.setAsync(lRpcReference.async());
                refObj = rpcReference.get(rpcReferenceWrapper);
                field.set(bean, refObj);
                client.doSubscribeService(field.getType());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return bean;
    }
}
