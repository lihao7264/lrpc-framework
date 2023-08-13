package com.atlihao.lrpc.framework.core.common;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.Semaphore;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 8:09 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 8:09 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class ServerServiceSemaphoreWrapper {

    private Semaphore semaphore;

    private int maxNums;

    public ServerServiceSemaphoreWrapper(int maxNums) {
        this.maxNums = maxNums;
        this.semaphore = new Semaphore(maxNums);
    }

}
