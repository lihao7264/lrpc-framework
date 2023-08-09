package com.atlihao.lrpc.framework.core.proxy.javassist.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 10:00 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 10:00 上午
 * @Version: 1.0.0
 */
public class Demo {

    public void doTest() {
        System.out.println("this is demo");
    }

    public String findStr() {
        return "success";
    }

    public List<String> findList() {
        return new ArrayList<>();
    }
}
