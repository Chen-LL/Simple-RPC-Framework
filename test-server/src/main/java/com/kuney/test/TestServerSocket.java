package com.kuney.test;

import com.kuney.rpc.protocol.socket.SocketServer;
import com.kuney.rpc.registry.DefaultServiceRegistry;
import com.kuney.rpc.registry.ServiceRegistry;

/**
 * @author kuneychen
 * @since 2022/7/12 18:08
 */
public class TestServerSocket {

    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(new HelloServiceImpl());
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(8080);
    }

}
