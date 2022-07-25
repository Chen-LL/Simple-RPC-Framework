package com.kuney.test;

import com.kuney.rpc.annotation.ServiceScan;
import com.kuney.rpc.transport.netty.server.NettyServer;

/**
 * @author kuneychen
 * @since 2022/7/19 22:07
 */
@ServiceScan
public class TestNettyServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start();
    }
}
