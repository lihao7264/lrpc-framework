package com.atlihao.lrpc.framework.spring.starter.config;

import com.atlihao.lrpc.framework.core.common.event.LRpcListenerLoader;
import com.atlihao.lrpc.framework.core.server.ApplicationShutdownHook;
import com.atlihao.lrpc.framework.core.server.Server;
import com.atlihao.lrpc.framework.core.server.ServiceWrapper;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/13 5:19 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/13 5:19 下午
 * @Version: 1.0.0
 */
@Slf4j
public class LRpcServerAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(LRpcService.class);
        if (beanMap.size() == 0) {
            // 说明当前应用内部无需对外暴露服务
            return;
        }
        printBanner();
        long begin = System.currentTimeMillis();
        Server server = new Server();
        server.initServerConfig();
        LRpcListenerLoader lRpcListenerLoader = new LRpcListenerLoader();
        lRpcListenerLoader.init();
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object bean = entry.getValue();
            LRpcService lRpcService = bean.getClass().getAnnotation(LRpcService.class);
            ServiceWrapper dataServiceServiceWrapper = new ServiceWrapper(bean, lRpcService.group());
            dataServiceServiceWrapper.setServiceToken(lRpcService.serviceToken());
            dataServiceServiceWrapper.setLimit(lRpcService.limit());
            server.exportService(dataServiceServiceWrapper);
            log.info(">>>>>>>>>>>>>>> [lrpc] {} export success! >>>>>>>>>>>>>>> ", entry.getKey());
        }
        long end = System.currentTimeMillis();
        ApplicationShutdownHook.registryShutdownHook();
        server.startApplication();
        log.info(" ================== [{}] started success in {}s ================== ", server.getServerConfig().getApplicationName(), ((double) end - (double) begin) / 1000);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void printBanner() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("|||---------- LRpc Starting Now! ----------|||");
        System.out.println("==============================================");
        System.out.println("源代码地址: https://github.com/lihao7264/lrpc-framework");
        System.out.println("version: 1.0.0");
        System.out.println();
    }
}
