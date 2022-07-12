package com.kuney.rpc.protocol;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.protocol.socket.SocketClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author kuneychen
 * @since 2022/7/12 16:34
 */
public class ProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequest rpcRequest = RpcRequest.builder()
                        .interfaceName(clazz.getName())
                        .methodName(method.getName())
                        .paramTypes(method.getParameterTypes())
                        .params(args)
                        .build();
                URL url = new URL("localhost", 8080);
                SocketClient socketClient = new SocketClient();
                return ((RpcResponse)socketClient.send(rpcRequest, url)).getData();
            }
        });
    }

}
