package com.kuney.test;

import com.kuney.rpc.protocol.socket.SocketServer;

/**
 * @author kuneychen
 * @since 2022/7/12 18:08
 */
public class TestServerSocket {

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.register(new HelloServiceImpl(), 8080);
    }

}
