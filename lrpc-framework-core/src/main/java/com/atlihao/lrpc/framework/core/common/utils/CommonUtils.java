package com.atlihao.lrpc.framework.core.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
}
