package com.atlihao.lrpc.framework.interfaces.pay;

import java.util.List;

public interface PayRpcService {

    boolean doPay();

    List<String> getPayHistoryByGoodNo(String goodNo);
}
