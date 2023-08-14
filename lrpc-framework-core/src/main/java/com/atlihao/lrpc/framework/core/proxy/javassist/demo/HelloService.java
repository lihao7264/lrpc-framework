package com.atlihao.lrpc.framework.core.proxy.javassist.demo;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 10:00 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 10:00 上午
 * @Version: 1.0.0
 */
public interface HelloService {

    void say(String msg);

    String echo(String msg);

    String[] getHobbies();

}