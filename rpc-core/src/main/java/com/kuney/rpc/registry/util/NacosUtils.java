package com.kuney.rpc.registry.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kuney.rpc.config.ServerConfiguration;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kuneychen
 * @since 2022/7/21 17:12
 */
@Slf4j
public class NacosUtils {

    private static final Set<String> serviceNames = new HashSet<>();
    private static final NamingService namingService;
    private static final String SERVICE_ADDRESS = ServerConfiguration.getServiceAddress();
    private static URL url;

    static {
        namingService = getNamingService();
    }

    public static NamingService getNamingService() {
        try {
            return NamingFactory.createNamingService(SERVICE_ADDRESS);
        } catch (NacosException e) {
            log.error("连接Nacos失败：{}", e.getMessage());
            throw new RpcException(RpcError.FAILED_TO_CONNECT_SERVICE_REGISTRY);
        }
    }

    public static List<Instance> getAllInstances(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    public static void registerService(String serviceName, URL url) throws NacosException {
        namingService.registerInstance(serviceName, url.getHost(), url.getPort());
        NacosUtils.url = url;
        serviceNames.add(serviceName);
    }

    public static void deregisterServices() {
        if (!serviceNames.isEmpty() && url != null) {
            for (String serviceName : serviceNames) {
                try {
                    namingService.deregisterInstance(serviceName, url.getHost(), url.getPort());
                } catch (NacosException e) {
                    log.error("注销 [{}] 服务失败: {}", serviceName, e.getMessage());
                }
            }
        }
    }

}
