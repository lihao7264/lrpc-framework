package com.atlihao.lrpc.framework.core.spi.jdk.test;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/11 8:18 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/11 8:18 下午
 * @Version: 1.0.0
 */
public class DefaultLSpiTest implements LSpiTest {
    @Override
    public void doTest() {
        System.out.println("测试");
    }
}
