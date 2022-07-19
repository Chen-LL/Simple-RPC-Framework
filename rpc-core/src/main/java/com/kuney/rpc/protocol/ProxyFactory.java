package com.kuney.rpc.protocol;

import com.kuney.rpc.entity.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author kuneychen
 * @since 2022/7/12 16:34
 */
public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz, RpcClient client) {
        return (T) Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest rpcRequest = RpcRequest.builder()
                        .interfaceName(clazz.getName())
                        .methodName(method.getName())
                        .paramTypes(method.getParameterTypes())
                        .params(args)
                        .build();
                return client.send(rpcRequest);
            }
        });
    }

}
