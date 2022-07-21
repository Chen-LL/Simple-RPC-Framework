package com.kuney.rpc.protocol;

/**
 * @author kuneychen
 * @since 2022/7/13 15:49
 */
public interface RpcServer {

    void start();

    <T> void publishService(Object service, Class<T> interfaceClass);

}
