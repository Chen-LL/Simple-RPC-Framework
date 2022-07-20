package com.kuney.test;

import com.kuney.rpc.api.Student;
import com.kuney.rpc.api.StudentService;
import com.kuney.rpc.protocol.ProxyFactory;
import com.kuney.rpc.protocol.netty.client.NettyClient;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/19 22:05
 */
public class TestNettyClient {
    public static void main(String[] args) {
        NettyClient client = new NettyClient("localhost", 8080);
        // HelloService helloService = ProxyFactory.getProxy(HelloService.class, client);
        // Object result = helloService.hello("netty client");
        StudentService studentService = ProxyFactory.getProxy(StudentService.class, client);
        // Student result = studentService.getStudent();
        List<Student> result = studentService.createList(10);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
