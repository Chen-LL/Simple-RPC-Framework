package com.kuney.test;

import com.kuney.rpc.api.HelloService;
import com.kuney.rpc.api.StudentService;
import com.kuney.rpc.protocol.socket.SocketServer;

/**
 * @author kuneychen
 * @since 2022/7/12 18:08
 */
public class TestServerSocket {

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer("192.168.3.18", 9090);
        socketServer.publishService(new HelloServiceImpl(), HelloService.class);
        socketServer.publishService(new StudentServiceImpl(), StudentService.class);
        socketServer.start();
    }

}
