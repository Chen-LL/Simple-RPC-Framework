package com.kuney.test;

import com.kuney.rpc.annotation.RpcServiceScan;
import com.kuney.rpc.transport.netty.server.NettyServer;

/**
 * @author kuneychen
 * @since 2022/7/19 22:07
 */
@RpcServiceScan("com.kuney.test.api")
public class TestNettyServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.start();
    }
}
