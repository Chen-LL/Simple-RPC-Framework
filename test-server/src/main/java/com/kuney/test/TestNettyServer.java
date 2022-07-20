package com.kuney.test;

import com.kuney.rpc.protocol.netty.server.NettyServer;
import com.kuney.rpc.registry.DefaultServiceRegistry;
import com.kuney.rpc.registry.ServiceRegistry;

/**
 * @author kuneychen
 * @since 2022/7/19 22:07
 */
public class TestNettyServer {
    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(new HelloServiceImpl());
        serviceRegistry.register(new StudentServiceImpl());

        NettyServer nettyServer = new NettyServer();
        nettyServer.start(8080);
    }
}
