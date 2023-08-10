package com.atlihao.lrpc.framework.core.registry.zookeeper;

import com.atlihao.lrpc.framework.core.common.event.LRpcEvent;
import com.atlihao.lrpc.framework.core.common.event.LRpcListenerLoader;
import com.atlihao.lrpc.framework.core.common.event.LRpcUpdateEvent;
import com.atlihao.lrpc.framework.core.common.event.data.URLChangeWrapper;
import com.atlihao.lrpc.framework.core.registry.RegistryService;
import com.atlihao.lrpc.framework.core.registry.URL;
import com.atlihao.lrpc.framework.interfaces.DataService;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 5:59 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 5:59 下午
 * @Version: 1.0.0
 */
public class ZookeeperRegister extends AbstractRegister implements RegistryService {

    private AbstractZookeeperClient zkClient;

    private String ROOT = "/lrpc";

    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParameters().get("host") + ":";
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
        //监听是否有新的服务注册
        String newServerNodePath = ROOT + "/" + url.getServiceName() + "/provider";
        watchChildNodeData(newServerNodePath);
    }

    public void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent);
                String path = watchedEvent.getPath();
                List<String> childrenDataList = zkClient.getChildrenData(path);
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(path.split("/")[2]);
                LRpcEvent lRpcEvent = new LRpcUpdateEvent(urlChangeWrapper);
                LRpcListenerLoader.sendEvent(lRpcEvent);
                // 收到回调后在注册一次监听，这样能保证一直都收到消息
                watchChildNodeData(path);
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
        List<String> urls = zookeeperRegister.getProviderIps(DataService.class.getName());
        System.out.println(urls);
        Thread.sleep(2000000);
    }
}
