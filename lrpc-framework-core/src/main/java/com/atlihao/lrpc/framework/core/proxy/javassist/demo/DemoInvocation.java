package com.atlihao.lrpc.framework.core.proxy.javassist.demo;

import com.atlihao.lrpc.framework.interfaces.DataService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 10:00 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 10:00 上午
 * @Version: 1.0.0
 */
public class DemoInvocation implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("this is invoke");
        return new Object();
    }

    public static void main(String[] args) throws Throwable {
        Method[] methods = DataService.class.getDeclaredMethods();
//        Demo$Proxy demo$Proxy = new Demo$Proxy(new DemoInvocation());
//        Demo$Proxy.methods = methods;
//        demo$Proxy.getList();
    }
}


