package com.kuney.rpc.factory;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.ResponseCode;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.registry.NacosServiceDiscovery;
import com.kuney.rpc.registry.ServiceDiscovery;
import com.kuney.rpc.transport.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

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

        private static final String INTERFACE_NAME = "interface name";
        private RpcClient client;

        public ClientProxy(RpcClient client) {
            this.client = client;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RpcRequest rpcRequest = new RpcRequest(
                    UUID.randomUUID().toString(),
                    method.getDeclaringClass().getCanonicalName(),
                    method.getName(),
                    method.getParameterTypes(),
                    args
            );
            URL url = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            RpcResponse rpcResponse = (RpcResponse) client.send(rpcRequest, url);
            this.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        }

        private void check(RpcRequest request, RpcResponse response) {
            String interfaceName = INTERFACE_NAME + ":" + request.getInterfaceName();
            if (response == null || response.getCode() == null || ResponseCode.SUCCESS.getCode() != response.getCode()) {
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, interfaceName);
            }
            if (!request.getRequestId().equals(response.getRequestId())) {
                throw new RpcException(RpcError.REQUEST_NOT_MATCH_WITH_RESPONSE, interfaceName);
            }
        }
    }

}
