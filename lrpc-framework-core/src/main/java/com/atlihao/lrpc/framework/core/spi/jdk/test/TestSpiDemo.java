package com.atlihao.lrpc.framework.core.spi.jdk.test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 8:13 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 8:13 下午
 * @Version: 1.0.0
 */
public class TestSpiDemo {

    public static void doTest(LSpiTest lSpiTest) {
        System.out.println("begin");
        lSpiTest.doTest();
        System.out.println("end");
    }

    public static void main(String[] args) {
        ServiceLoader<LSpiTest> serviceLoader = ServiceLoader.load(LSpiTest.class);
        Iterator<LSpiTest> lSpiTestIterator = serviceLoader.iterator();
        while (lSpiTestIterator.hasNext()) {
            LSpiTest lSpiTest = lSpiTestIterator.next();
            TestSpiDemo.doTest(lSpiTest);
        }
    }

}
