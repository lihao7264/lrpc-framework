package com.atlihao.framework.good.provider.service;

import com.atlihao.lrpc.framework.interfaces.good.GoodRpcService;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcService;

import java.util.Arrays;
import java.util.List;


@LRpcService
public class GoodRpcServiceImpl implements GoodRpcService {

    @Override
    public boolean decreaseStock() {
        return true;
    }

    @Override
    public List<String> selectGoodsNoByUserId(String userId) {
        return Arrays.asList(userId + "-good-01", userId + "-good-02");
    }
}
