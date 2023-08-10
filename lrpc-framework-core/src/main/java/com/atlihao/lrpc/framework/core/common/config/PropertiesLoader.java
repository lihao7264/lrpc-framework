package com.atlihao.lrpc.framework.core.common.config;

import com.atlihao.lrpc.framework.core.common.utils.CommonUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description: 配置加载器
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:44 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:44 下午
 * @Version: 1.0.0
 */
public class PropertiesLoader {

    /**
     * 配置文件信息
     */
    private static Properties properties;

    /**
     * 配置文件信息
     */
    private static Map<String, String> propertiesMap = new HashMap<>();

    /**
     * 配置文件地址
     */
    private static String DEFAULT_PROPERTIES_FILE = "/Users/admin/Desktop/学习/正在进行/源码/rpc/lrpc-framework/lrpc-framework-core/src/main/resources/lrpc.properties";

    /**
     * 加载配置文件
     *
     * @throws IOException
     */
    public static void loadConfiguration() throws IOException {
        if (properties != null) {
            return;
        }
        properties = new Properties();
        FileInputStream in = null;
        in = new FileInputStream(DEFAULT_PROPERTIES_FILE);
        properties.load(in);
    }

    /**
     * 根据键值获取配置属性
     *
     * @param key
     * @return
     */
    public static String getPropertiesStr(String key) {
        if (properties == null) {
            return null;
        }
        if (CommonUtils.isEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return String.valueOf(propertiesMap.get(key));
    }

    /**
     * 根据键值获取配置属性
     *
     * @param key
     * @return
     */
    public static Integer getPropertiesInteger(String key) {
        if (properties == null) {
            return null;
        }
        if (CommonUtils.isEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return Integer.valueOf(propertiesMap.get(key));
    }
}