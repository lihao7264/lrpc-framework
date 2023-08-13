package com.atlihao.lrpc.framework.provider.springboot.service.impl;


import com.atlihao.lrpc.framework.interfaces.UserService;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcService;

@LRpcService
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("test");
    }
}
