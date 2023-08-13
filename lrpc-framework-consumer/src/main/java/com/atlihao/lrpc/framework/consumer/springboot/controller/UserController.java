package com.atlihao.lrpc.framework.consumer.springboot.controller;

import com.atlihao.lrpc.framework.interfaces.OrderService;
import com.atlihao.lrpc.framework.interfaces.UserService;
import com.atlihao.lrpc.framework.spring.starter.common.LRpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author lihao726726
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @LRpcReference
    private UserService userService;

    /**
     * 验证各类参数配置是否异常
     */
    @LRpcReference(group = "order-group", serviceToken = "order-token")
    private OrderService orderService;

    @GetMapping(value = "/test")
    public void test() {
        userService.test();
    }


    @GetMapping(value = "/testMaxData")
    public String testMaxData(int i) {
        String result = orderService.testMaxData(i);
        System.out.println(result.length());
        return result;
    }


    @GetMapping(value = "/get-order-no")
    public List<String> getOrderNo() {
        List<String> result = orderService.getOrderNoList();
        System.out.println(result);
        return result;
    }


}
