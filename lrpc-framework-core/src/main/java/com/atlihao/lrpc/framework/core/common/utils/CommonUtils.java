package com.atlihao.lrpc.framework.core.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:30 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:30 上午
 * @Version: 1.0.0
 */
public class CommonUtils {

    /**
     * 获取目标对象的实现接口
     *
     * @param targetClass
     * @return
     */
    public static List<Class<?>> getAllInterfaces(Class targetClass) {
        if (Objects.isNull(targetClass)) {
            throw new IllegalArgumentException("targetClass is null!");
        }
        Class[] interfaces = targetClass.getInterfaces();
        if (interfaces.length == 0) {
            return Collections.emptyList();
        }
        List<Class<?>> classes = new ArrayList<>(interfaces.length);
        for (Class aClass : interfaces) {
            classes.add(aClass);
        }
        return classes;
    }


    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return "";
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmptyList(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmptyList(List list) {
        return !isEmptyList(list);
    }
}
