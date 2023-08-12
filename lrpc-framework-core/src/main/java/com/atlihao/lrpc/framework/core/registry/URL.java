package com.atlihao.lrpc.framework.core.registry;

import com.atlihao.lrpc.framework.core.registry.zookeeper.ProviderNodeInfo;
import lombok.Data;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:35 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:35 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class URL {


    /**
     * 服务应用名称
     */
    private String applicationName;

    /**
     * 注册到节点到服务名称，例如：com.atlihao.test.UserService
     */
    private String serviceName;

    /**
     * 这里面可自定义不限进行扩展
     * 分组
     * 权重
     * 服务提供者的地址
     * 服务提供者的端口
     */
    private Map<String, String> parameters = new HashMap<>();


    public void addParameter(String key, String value) {
        this.parameters.putIfAbsent(key, value);
    }

    /**
     * 将URL转换为写入zk的provider节点下的一段字符串
     *
     * @param url
     * @return
     */
    public static String buildProviderUrlStr(URL url) {
        String host = url.getParameters().get("host");
        String port = url.getParameters().get("port");
        String group = url.getParameters().get("group");
        return new String((url.getApplicationName() + ";" + url.getServiceName() + ";" + host + ":" + port + ";" +
                System.currentTimeMillis() + ";100;" + group).getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 将URL转换为写入zk的consumer节点下的一段字符串
     *
     * @param url
     * @return
     */
    public static String buildConsumerUrlStr(URL url) {
        String host = url.getParameters().get("host");
        return new String((url.getApplicationName() + ";" + url.getServiceName() + ";" + host + ";" +
                System.currentTimeMillis()).getBytes(), StandardCharsets.UTF_8);
    }


    /**
     * 将某个节点下的信息转换为一个Provider节点对象
     *
     * @param providerNodeStr
     * @return
     */
    public static ProviderNodeInfo buildURLFromUrlStr(String providerNodeStr) {
        String[] items = providerNodeStr.split(";");
        ProviderNodeInfo providerNodeInfo = new ProviderNodeInfo();
        providerNodeInfo.setApplicationName(items[0]);
        providerNodeInfo.setServiceName(items[1]);
        providerNodeInfo.setAddress(items[2]);
        providerNodeInfo.setRegistryTime(items[3]);
        providerNodeInfo.setWeight(Integer.valueOf(items[4]));
        providerNodeInfo.setGroup(String.valueOf(items[5]));
        return providerNodeInfo;
    }

    /**
     * ProviderNodeInfo(serviceName=com.atlihao.lrpc.framework.interfaces.DataService, address=10.1.21.11:9092)
     *
     * @param args
     */
    public static void main(String[] args) {
        ProviderNodeInfo providerNodeInfo = buildURLFromUrlStr("lrpc-provider;com.atlihao.lrpc.framework.interfaces.DataService;10.1.21.11:9092;1643429082637;100;default");
        System.out.println(providerNodeInfo);
    }
}
