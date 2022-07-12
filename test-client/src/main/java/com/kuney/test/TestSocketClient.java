package com.kuney.test;

import com.kuney.rpc.api.HelloService;
import com.kuney.rpc.protocol.ProxyFactory;

/**
 * @author kuneychen
 * @since 2022/7/12 17:58
 */
public class TestSocketClient {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        Object result = helloService.hello("socket");
        System.out.println(result);
    }
}
