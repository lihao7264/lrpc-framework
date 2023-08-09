package com.atlihao.lrpc.framework.core.server;

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
        System.out.println("己收到的参数长度：" + body.length());
        return "success";
    }

    @Override
    public List<String> getList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("test1");
        arrayList.add("test2");
        arrayList.add("test3");
        return arrayList;
    }
}
