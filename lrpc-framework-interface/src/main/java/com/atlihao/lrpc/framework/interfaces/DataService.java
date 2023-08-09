package com.atlihao.lrpc.framework.interfaces;

import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:15 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:15 上午
 * @Version: 1.0.0
 */
public interface DataService {

    /**
     * 发送数据
     *
     * @param body
     */
    String sendData(String body);

    /**
     * 获取数据
     *
     * @return
     */
    List<String> getList();
}
