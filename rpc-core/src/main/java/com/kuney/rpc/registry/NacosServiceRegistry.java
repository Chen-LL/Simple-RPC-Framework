package com.kuney.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.protocol.URL;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/20 21:36
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    // TODO 读取配置文件
    private static final String SERVER_ADDRESS = "127.0.0.1:8848";
    private static final NamingService namingService;

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDRESS);
        } catch (NacosException e) {
            log.error("连接Nacos失败：{}", e.getMessage());
            throw new RpcException(RpcError.FAILED_TO_CONNECT_SERVICE_REGISTRY);
        }
    }

    @Override
    public void register(String serviceName, URL url) {
        try {
            namingService.registerInstance(serviceName, url.getHost(), url.getPort());
        } catch (NacosException e) {
            log.error("注册服务失败：{}", e.getMessage());
            throw new RpcException(RpcError.FAILED_TO_REGISTER_SERVICE);
        }
    }

    @Override
    public URL lookupService(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            // TODO 负载均衡
            Instance instance = instances.get(0);
            return new URL(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务失败：{}", e.getMessage());
            throw new RpcException(RpcError.FAILED_TO_LOOKUP_SERVICE);
        }
    }
}
