package com.kuney.test;

import com.kuney.rpc.api.HelloService;
import com.kuney.rpc.factory.ClientProxyFactory;
import com.kuney.rpc.transport.netty.client.NettyClient;

/**
 * @author kuneychen
 * @since 2022/7/19 22:05
 */
public class TestNettyClient {
    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        HelloService helloService = ClientProxyFactory.getProxy(HelloService.class, client);
        Object result = helloService.hello("netty client");
        // StudentService studentService = ClientProxyFactory.getProxy(StudentService.class, client);
        // Student result = studentService.getStudent();
        // List<Student> result = studentService.createList(10);
        System.out.println(result);
    }
}
