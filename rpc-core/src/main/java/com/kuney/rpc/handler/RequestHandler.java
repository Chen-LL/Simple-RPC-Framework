package com.kuney.rpc.handler;

import com.kuney.rpc.transport.dto.RpcRequest;
import com.kuney.rpc.transport.dto.RpcResponse;
import com.kuney.rpc.enums.ResponseCode;
import com.kuney.rpc.factory.SingletonFactory;
import com.kuney.rpc.registry.LocalServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author kuneychen
 * @since 2022/7/12 23:49
 */
@Slf4j
public class RequestHandler {

    private static LocalServiceProvider serviceProvider;

    static {
        serviceProvider = SingletonFactory.getInstance(LocalServiceProvider.class);
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object result = method.invoke(service, rpcRequest.getParams());
            log.info("服务：{} 成功调用方法: {}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
    }
}
