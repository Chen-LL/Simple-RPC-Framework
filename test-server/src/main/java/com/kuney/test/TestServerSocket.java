package com.kuney.test;

import com.kuney.rpc.transport.socket.SocketServer;

/**
 * @author kuneychen
 * @since 2022/7/12 18:08
 */
public class TestServerSocket {

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer("192.168.3.18", 9090);
        socketServer.start();
    }

}
