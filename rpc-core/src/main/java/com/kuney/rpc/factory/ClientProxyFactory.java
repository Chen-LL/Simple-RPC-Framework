package com.kuney.rpc.factory;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.protocol.RpcClient;
import com.kuney.rpc.protocol.URL;
import com.kuney.rpc.registry.NacosServiceRegistry;
import com.kuney.rpc.registry.ServiceRegistry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author kuneychen
 * @since 2022/7/12 16:34
 */
public class ClientProxyFactory {

    private static final ServiceRegistry serviceRegistry;

    static {
        serviceRegistry = new NacosServiceRegistry();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz, RpcClient client) {
        return (T) Proxy.newProxyInstance(ClientProxyFactory.class.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest rpcRequest = RpcRequest.builder()
                        .interfaceName(clazz.getName())
                        .methodName(method.getName())
                        .paramTypes(method.getParameterTypes())
                        .params(args)
                        .build();
                URL url = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
                RpcResponse response = (RpcResponse) client.send(rpcRequest, url);
                return response.getData();
            }
        });
    }

}
