package com.atlihao.lrpc.framework.core.service.impl;

import com.atlihao.lrpc.framework.interfaces.DataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:14 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:14 下午
 * @Version: 1.0.0
 */
public class DataServiceImpl implements DataService {

    @Override
    public String sendData(String body) {
        try {
            Thread.sleep(8000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("己收到的参数长度：" + body.length());
        return "success";
    }

    @Override
    public List<String> getList() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("test1");
        arrayList.add("test2");
        arrayList.add("test3");
        return arrayList;
    }

    @Override
    public void testError() {
        System.out.println(1 / 0);
    }

    @Override
    public String testErrorV2() {
        throw new RuntimeException("测试异常");
//        return "three";
    }
}
