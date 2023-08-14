package com.atlihao.lrpc.framework.common.consumer.controller;

import com.atlihao.lrpc.framework.interfaces.good.GoodRpcService;
import com.atlihao.lrpc.framework.interfaces.pay.PayRpcService;
import com.atlihao.lrpc.framework.interfaces.user.UserRpcService;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api-test")
public class ApiTestController {

    @LRpcReference
    private UserRpcService userRpcService;
    @LRpcReference
    private GoodRpcService goodRpcService;
    @LRpcReference
    private PayRpcService payRpcService;

    @GetMapping(value = "/do-test")
    public boolean doTest() {
        long begin1 = System.currentTimeMillis();
        userRpcService.getUserId();
        long end1 = System.currentTimeMillis();
        System.out.println("userRpc--->" + (end1 - begin1) + "ms");
        long begin2 = System.currentTimeMillis();
        goodRpcService.decreaseStock();
        long end2 = System.currentTimeMillis();
        System.out.println("goodRpc--->" + (end2 - begin2) + "ms");
        long begin3 = System.currentTimeMillis();
        payRpcService.doPay();
        long end3 = System.currentTimeMillis();
        System.out.println("payRpc--->" + (end3 - begin3) + "ms");
        return true;
    }


    @GetMapping(value = "/do-test-2")
    public void doTest2() {
        String userId = userRpcService.getUserId();
        System.out.println("userRpcService result: " + userId);
        boolean goodResult = goodRpcService.decreaseStock();
        System.out.println("goodRpcService result: " + goodResult);
    }
}
