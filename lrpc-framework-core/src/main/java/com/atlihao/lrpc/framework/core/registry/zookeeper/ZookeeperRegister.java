package com.atlihao.lrpc.framework.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.atlihao.lrpc.framework.core.common.event.LRpcEvent;
import com.atlihao.lrpc.framework.core.common.event.LRpcListenerLoader;
import com.atlihao.lrpc.framework.core.common.event.LRpcNodeChangeEvent;
import com.atlihao.lrpc.framework.core.common.event.LRpcUpdateEvent;
import com.atlihao.lrpc.framework.core.common.event.data.URLChangeWrapper;
import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;
import com.atlihao.lrpc.framework.core.registry.RegistryService;
import com.atlihao.lrpc.framework.core.registry.URL;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.CLIENT_CONFIG;
import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.IS_STARTED;
import static com.atlihao.lrpc.framework.core.common.cache.CommonServerCache.SERVER_CONFIG;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 5:59 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 5:59 下午
 * @Version: 1.0.0
 */
@Data
public class ZookeeperRegister extends AbstractRegister implements RegistryService {

    private AbstractZookeeperClient zkClient;

    private String ROOT = "/lrpc";

    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParameters().get("host") + ":";
    }

    public ZookeeperRegister() {
        String registryAddr = CLIENT_CONFIG != null ? CLIENT_CONFIG.getRegisterAddr() : SERVER_CONFIG.getRegisterAddr();
        this.zkClient = new CuratorZookeeperClient(registryAddr);
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }


    @Override
    public List<String> getProviderIps(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        return nodeDataList;
    }

    @Override
    public Map<String, String> getServiceWeightMap(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        Map<String, String> result = new HashMap<>();
        for (String ipAndHost : nodeDataList) {
            String childData = this.zkClient.getNodeData(ROOT + "/" + serviceName + "/provider/" + ipAndHost);
            result.put(ipAndHost, childData);
        }
        return result;
    }

    @Override
    public void register(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildProviderUrlStr(url);
        if (!zkClient.existNode(getProviderPath(url))) {
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        } else {
            zkClient.deleteNode(getProviderPath(url));
            zkClient.createTemporaryData(getProviderPath(url), urlStr);
        }
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        if (!IS_STARTED) {
            return;
        }
        zkClient.deleteNode(getProviderPath(url));
        super.unRegister(url);
    }

    @Override
    public void subscribe(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildConsumerUrlStr(url);
        if (!zkClient.existNode(getConsumerPath(url))) {
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        } else {
            zkClient.deleteNode(getConsumerPath(url));
            zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        }
        super.subscribe(url);
    }

    @Override
    public void doAfterSubscribe(URL url) {
        // 监听是否有新的服务注册
        String servicePath = url.getParameters().get("servicePath");
        String newServerNodePath = ROOT + "/" + servicePath;
        // 订阅节点地址：/lrpc/com.atlihao.lrpc.UserService/provider
        watchChildNodeData(newServerNodePath);
        String providerIpStrJson = url.getParameters().get("providerIps");
        List<String> providerIpList = JSON.parseObject(providerIpStrJson, List.class);
        for (String providerIp : providerIpList) {
            // 启动环节会触发订阅订阅节点详情地址：/lrpc/com.atlihao.lrpc.UserService/provider/192.11.11.101:9090
            this.watchNodeDataChange(ROOT + "/" + servicePath + "/" + providerIp);
        }
    }


    /**
     * 订阅服务节点内部的数据变化
     *
     * @param newServerNodePath
     */
    public void watchNodeDataChange(String newServerNodePath) {
        zkClient.watchNodeData(newServerNodePath, new Watcher() {

            @Override
            public void process(WatchedEvent watchedEvent) {
                String path = watchedEvent.getPath();
                System.out.println("[watchNodeDataChange] 监听到zk节点下的" + path + "节点数据发生变更");
                String nodeData = zkClient.getNodeData(path);
                if(StringUtils.isNotBlank(nodeData)){
                    ProviderNodeInfo providerNodeInfo = URL.buildURLFromUrlStr(nodeData);
                    LRpcEvent iRpcEvent = new LRpcNodeChangeEvent(providerNodeInfo);
                    LRpcListenerLoader.sendEvent(iRpcEvent);
                }
                watchNodeDataChange(newServerNodePath);
            }
        });
    }

    /**
     * 监听子节点数据变化
     *
     * @param newServerNodePath
     */
    public void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                String servicePath = watchedEvent.getPath();
                System.out.println("收到子节点" + servicePath + "数据变化");
                List<String> childrenDataList = zkClient.getChildrenData(servicePath);
                if (CommonUtils.isEmptyList(childrenDataList)) {
                    watchChildNodeData(servicePath);
                    return;
                }
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                Map<String, String> nodeDetailInfoMap = new HashMap<>();
                for (String providerAddress : childrenDataList) {
                    String nodeDetailInfo = zkClient.getNodeData(servicePath + "/" + providerAddress);
                    nodeDetailInfoMap.put(providerAddress, nodeDetailInfo);
                }
                urlChangeWrapper.setNodeDataUrl(nodeDetailInfoMap);
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(servicePath.split("/")[2]);
                LRpcEvent lRpcEvent = new LRpcUpdateEvent(urlChangeWrapper);
                LRpcListenerLoader.sendEvent(lRpcEvent);
                // 收到回调后在注册一次监听，这样能保证一直都收到消息
                watchChildNodeData(servicePath);
                for (String providerAddress : childrenDataList) {
                    watchNodeDataChange(servicePath + "/" + providerAddress);
                }
            }
        });
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public void doUnSubscribe(URL url) {
        this.zkClient.deleteNode(getConsumerPath(url));
        super.doUnSubscribe(url);
    }

    public static void main(String[] args) throws InterruptedException {
        ZookeeperRegister zookeeperRegister = new ZookeeperRegister("localhost:2181");
        AbstractZookeeperClient abstractZookeeperClient = zookeeperRegister.getZkClient();
        String path = "/lrpc/com.atlihao.lrpc.framework.interfaces.DataService/provider/192.168.43.227:9093";
        String nodeData = abstractZookeeperClient.getNodeData(path);
        abstractZookeeperClient.watchNodeData(path, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.getPath());
            }
        });
        Thread.sleep(2000000);
    }
}
