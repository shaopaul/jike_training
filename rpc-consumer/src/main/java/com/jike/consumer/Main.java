package com.jike.consumer;

import com.jike.consumer.service.SkuService;
import com.jike.consumer.service.UserService;
import com.jike.consumer.stub.PaulRPCProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("call skuService  ...");
        SkuService skuService = (SkuService) PaulRPCProxy.create(SkuService.class);
        String result = skuService.findByName("hello");
        log.info("get result : {}", result);

        log.info("call userService ...");
        UserService userService =(UserService) PaulRPCProxy.create(UserService.class);
        String result2 = userService.findById();
        log.info("get result: {}", result2);
    }
}