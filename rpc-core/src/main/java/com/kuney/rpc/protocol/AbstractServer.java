package com.kuney.rpc.protocol;

import com.kuney.rpc.registry.LocalServiceProvider;
import com.kuney.rpc.registry.ServiceRegistry;

/**
 * @author kuneychen
 * @since 2022/7/20 22:25
 */
public abstract class AbstractServer implements RpcServer {

    protected String host;
    protected int port;
    protected LocalServiceProvider serviceProvider;
    protected ServiceRegistry serviceRegistry;

    @Override
    public <T> void publishService(Object service, Class<T> interfaceClass) {
        serviceProvider.register(service);
        serviceRegistry.register(interfaceClass.getCanonicalName(), new URL(host, port));
    }
}
