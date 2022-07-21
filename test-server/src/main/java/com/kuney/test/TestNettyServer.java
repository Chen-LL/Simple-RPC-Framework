package com.kuney.test;

import com.kuney.rpc.api.HelloService;
import com.kuney.rpc.api.StudentService;
import com.kuney.rpc.protocol.netty.server.NettyServer;

/**
 * @author kuneychen
 * @since 2022/7/19 22:07
 */
public class TestNettyServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer("127.0.0.1", 8080);
        nettyServer.publishService(new HelloServiceImpl(), HelloService.class);
        nettyServer.publishService(new StudentServiceImpl(), StudentService.class);
        nettyServer.start();
    }
}
