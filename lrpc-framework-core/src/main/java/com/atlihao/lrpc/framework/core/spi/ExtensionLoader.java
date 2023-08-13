package com.atlihao.lrpc.framework.core.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.EXTENSION_LOADER;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 8:18 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 8:18 下午
 * @Version: 1.0.0
 */
public class ExtensionLoader {

    public static String EXTENSION_LOADER_DIR_PREFIX = "META-INF/lrpc/";

    public static Map<String, LinkedHashMap<String, Class>> EXTENSION_LOADER_CLASS_CACHE = new ConcurrentHashMap<>();

    public void loadExtension(Class clazz) throws IOException, ClassNotFoundException {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null!");
        }
        if (EXTENSION_LOADER_CLASS_CACHE.containsKey(clazz.getName())) {
            return;
        }
        String spiFilePath = EXTENSION_LOADER_DIR_PREFIX + clazz.getName();
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<URL> enumeration = null;
        enumeration = classLoader.getResources(spiFilePath);
        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();
            InputStreamReader inputStreamReader = null;
            inputStreamReader = new InputStreamReader(url.openStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            LinkedHashMap<String, Class> classMap = new LinkedHashMap<>();
            while ((line = bufferedReader.readLine()) != null) {
                //如果配置中加入了#开头则表示忽略该类无需进行加载
                if (line.startsWith("#")) {
                    continue;
                }
                String[] lineArr = line.split("=");
                String implClassName = lineArr[0];
                String interfaceName = lineArr[1];
                classMap.put(implClassName, Class.forName(interfaceName));
            }
            // 只会触发class文件的加载，而不会触发对象的实例化
            if (EXTENSION_LOADER_CLASS_CACHE.containsKey(clazz.getName())) {
                // 支持开发者自定义配置
                EXTENSION_LOADER_CLASS_CACHE.get(clazz.getName()).putAll(classMap);
            } else {
                EXTENSION_LOADER_CLASS_CACHE.put(clazz.getName(), classMap);
            }
        }
    }

    public Object loadExtensionInstance(Class clazz, String targetCode) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        EXTENSION_LOADER.loadExtension(clazz);
        LinkedHashMap<String, Class> extMap = EXTENSION_LOADER_CLASS_CACHE.get(clazz.getName());
        Class targetClass = extMap.get(targetCode);
        if (targetClass == null) {
            throw new RuntimeException("no match code for " + targetCode);
        }
        return targetClass.newInstance();
    }

    public List<Object> loadAllExtensionInstance(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        EXTENSION_LOADER.loadExtension(clazz);
        LinkedHashMap<String, Class> extClassMap = EXTENSION_LOADER_CLASS_CACHE.get(clazz.getName());
        List<Object> result = new ArrayList<>();
        for (String key : extClassMap.keySet()) {
            Class extClass = extClassMap.get(key);
            if (extClass == null) {
                throw new RuntimeException("no match key type for " + key);
            }
            result.add(extClass.newInstance());
        }
        return result;
    }
}
