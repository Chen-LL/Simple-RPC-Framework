package com.kuney.rpc.registry;

import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册，指定接口的对应实现类
 * @author kuneychen
 * @since 2022/7/12 22:35
 */
@Slf4j
public class LocalServiceProvider {

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    public <T> void register(String serviceName, T service) {
        if (serviceMap.containsKey(serviceName)) {
            return;
        }
        serviceMap.put(serviceName, service);
        log.info("向接口：{} 注册服务：{}", service.getClass().getInterfaces(), serviceName);
    }

    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
