package com.atlihao.lrpc.framework.core.tolerant.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 7:50 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 7:50 下午
 * @Version: 1.0.0
 */
public class SemaphoreTest {

    /**
     * 线程数
     */
    private static final int THREAD_COUNT = 30;

    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore semaphore = new Semaphore(10);

    /**
     * 一次只允许10个线程通过请求
     *
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("save data " + System.currentTimeMillis());
                        Thread.sleep(5000);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
    }
}
