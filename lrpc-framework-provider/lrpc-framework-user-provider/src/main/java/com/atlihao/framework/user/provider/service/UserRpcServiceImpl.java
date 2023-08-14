package com.atlihao.framework.user.provider.service;

import com.atlihao.lrpc.framework.interfaces.good.GoodRpcService;
import com.atlihao.lrpc.framework.interfaces.pay.PayRpcService;
import com.atlihao.lrpc.framework.interfaces.user.UserRpcService;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcReference;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcService;

import java.util.*;


@LRpcService
public class UserRpcServiceImpl implements UserRpcService {

    @LRpcReference
    private GoodRpcService goodRpcService;
    @LRpcReference
    private PayRpcService payRpcService;

    @Override
    public String getUserId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<Map<String, String>> findMyGoods(String userId) {
        List<String> goodsNoList = goodRpcService.selectGoodsNoByUserId(userId);
        List<Map<String, String>> finalResult = new ArrayList<>();
        goodsNoList.forEach(goodsNo -> {
            Map<String, String> item = new HashMap<>(2);
            List<String> payHistory = payRpcService.getPayHistoryByGoodNo(goodsNo);
            item.put(goodsNo, payHistory.toString());
            finalResult.add(item);
        });
        return finalResult;
    }
}
