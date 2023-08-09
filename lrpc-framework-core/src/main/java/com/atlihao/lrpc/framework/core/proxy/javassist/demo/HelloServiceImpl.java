package com.atlihao.lrpc.framework.core.proxy.javassist.demo;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 10:00 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 10:00 上午
 * @Version: 1.0.0
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public void say(String msg) {
        System.out.println("this is say");
    }

    @Override
    public String echo(String msg) {
        System.out.println("this is echo");
        return msg;
    }

    @Override
    public String[] getHobbies() {
        System.out.println("this is getHobbies");
        return new String[0];
    }
}
