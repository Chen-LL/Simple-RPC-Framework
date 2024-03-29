package com.kuney.test;

import com.kuney.rpc.annotation.RpcServiceScan;
import com.kuney.rpc.transport.socket.SocketServer;

/**
 * @author kuneychen
 * @since 2022/7/12 18:08
 */
@RpcServiceScan
public class TestServerSocket {

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.start();
    }

}
