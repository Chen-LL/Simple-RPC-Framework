package com.kuney.rpc.factory;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.transport.RpcClient;
import com.kuney.rpc.registry.NacosServiceDiscovery;
import com.kuney.rpc.registry.ServiceDiscovery;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author kuneychen
 * @since 2022/7/12 16:34
 */
public class ClientProxyFactory {

    private static final ServiceDiscovery serviceDiscovery;

    static {
        serviceDiscovery = new NacosServiceDiscovery();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz, RpcClient client) {
        return (T) Proxy.newProxyInstance(ClientProxyFactory.class.getClassLoader(), new Class<?>[]{clazz}, new ClientProxy(client));
    }

    private static class ClientProxy implements InvocationHandler {

        private RpcClient client;

        public ClientProxy(RpcClient client) {
            this.client = client;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RpcRequest rpcRequest = RpcRequest.builder()
                    .interfaceName(method.getDeclaringClass().getCanonicalName())
                    .methodName(method.getName())
                    .paramTypes(method.getParameterTypes())
                    .params(args)
                    .build();
            URL url = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            RpcResponse rpcResponse = (RpcResponse) client.send(rpcRequest, url);
            this.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        }

        private void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {

        }
    }

}
