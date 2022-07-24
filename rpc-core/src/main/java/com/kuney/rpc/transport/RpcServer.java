package com.kuney.rpc.transport;

/**
 * @author kuneychen
 * @since 2022/7/13 15:49
 */
public interface RpcServer {

    void start();

    <T> void publishService(String serviceName, Object service);

}
