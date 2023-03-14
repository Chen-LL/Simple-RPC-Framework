package com.kuney.rpc.factory;

import com.kuney.rpc.transport.dto.RpcRequest;
import com.kuney.rpc.transport.dto.RpcResponse;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.ResponseCode;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.registry.NacosServiceDiscovery;
import com.kuney.rpc.registry.ServiceDiscovery;
import com.kuney.rpc.transport.RpcClient;
import com.kuney.rpc.transport.netty.client.NettyClient;
import com.kuney.rpc.transport.socket.SocketClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author kuneychen
 * @since 2022/7/12 16:34
 */
public class ClientProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz, RpcClient client) {
        return (T) Proxy.newProxyInstance(ClientProxyFactory.class.getClassLoader(), new Class<?>[]{clazz}, new ClientProxy(client));
    }

    private static class ClientProxy implements InvocationHandler {

        private static ServiceDiscovery serviceDiscovery;

        static {
            serviceDiscovery = new NacosServiceDiscovery();
        }

        private RpcClient client;

        public ClientProxy(RpcClient client) {
            this.client = client;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RpcRequest rpcRequest = new RpcRequest(
                    UUID.randomUUID().toString(),
                    false,
                    method.getDeclaringClass().getCanonicalName(),
                    method.getName(),
                    method.getParameterTypes(),
                    args
            );
            URL url = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            RpcResponse rpcResponse = null;
            if (client instanceof NettyClient) {
                CompletableFuture<RpcResponse> resultFuture = (CompletableFuture<RpcResponse>) client.send(rpcRequest, url);
                rpcResponse = resultFuture.get();
            } else if (client instanceof SocketClient) {
                rpcResponse = (RpcResponse) client.send(rpcRequest, url);
            }
            this.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        }

        private void check(RpcRequest request, RpcResponse response) {
            String interfaceName = "interface name: " + request.getInterfaceName();
            if (response == null || response.getCode() == null || ResponseCode.SUCCESS.getCode() != response.getCode()) {
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, interfaceName);
            }
            if (!request.getRequestId().equals(response.getRequestId())) {
                throw new RpcException(RpcError.REQUEST_NOT_MATCH_WITH_RESPONSE, interfaceName);
            }
        }
    }

}
