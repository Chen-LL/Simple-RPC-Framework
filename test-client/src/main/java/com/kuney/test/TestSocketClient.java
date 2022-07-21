package com.kuney.test;

import com.kuney.rpc.api.HelloService;
import com.kuney.rpc.factory.ClientProxyFactory;
import com.kuney.rpc.protocol.socket.SocketClient;

/**
 * @author kuneychen
 * @since 2022/7/12 17:58
 */
public class TestSocketClient {
    public static void main(String[] args) {
        HelloService helloService = ClientProxyFactory.getProxy(HelloService.class, new SocketClient());
        Object result = helloService.hello("socket");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
