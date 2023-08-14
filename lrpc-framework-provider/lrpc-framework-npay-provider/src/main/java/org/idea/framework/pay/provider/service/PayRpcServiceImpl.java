package org.idea.framework.pay.provider.service;

import com.atlihao.lrpc.framework.interfaces.pay.PayRpcService;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcService;

import java.util.Arrays;
import java.util.List;

@LRpcService
public class PayRpcServiceImpl implements PayRpcService {

    @Override
    public boolean doPay() {
        System.out.println("pay1");
        return true;
    }

    @Override
    public List<String> getPayHistoryByGoodNo(String goodNo) {
        System.out.println("getPayHistoryByGoodNo");
        return Arrays.asList(goodNo + "-pay-001", goodNo + "-pay-002");
    }

}
