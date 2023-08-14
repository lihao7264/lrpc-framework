package com.atlihao.lrpc.framework.core.tolerant;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 限流器
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 7:47 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 7:47 下午
 * @Version: 1.0.0
 */
public class LimitRequestV1 {


    private int limit;

    private int times;

    private AtomicInteger request = new AtomicInteger(0);

    private TimeUnit timeUnit;

    public LimitRequestV1(int limit, int times, TimeUnit timeUnit) {
        this.limit = limit;
        this.times = times;
        this.timeUnit = timeUnit;
    }


    /**
     * 请求
     */
    public void doRequest() {
        synchronized (this) {
            if (request.get() >= limit) {
                throw new RuntimeException("请求超过限制流量");
            }
            request.incrementAndGet();
        }
    }

    /**
     * 请求后
     */
    public void afterRequest() {
        synchronized (this) {
            request.decrementAndGet();
        }
    }

    /**
     * 测试方法
     */
    public void testMethod() {
        this.doRequest();
        System.out.println("请求处理函数");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.afterRequest();
    }

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        LimitRequestV1 limitRequest = new LimitRequestV1(10, 1, TimeUnit.SECONDS);
        for (int i = 0; i < 13; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    limitRequest.testMethod();
                }
            });
            thread.start();
        }
        countDownLatch.countDown();
        System.out.println("============");
        Thread.yield();
    }
}
