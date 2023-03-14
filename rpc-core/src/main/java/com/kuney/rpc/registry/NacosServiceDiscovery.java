package com.kuney.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kuney.rpc.config.ClientConfiguration;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.loadbalance.LoadBalance;
import com.kuney.rpc.registry.util.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/21 21:03
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private LoadBalance loadBalance;

    public NacosServiceDiscovery() {
        loadBalance = LoadBalance.getByCode(ClientConfiguration.getLoadBalanceCode());
    }

    @Override
    public URL lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtils.getAllInstances(serviceName);
            if (instances == null || instances.size() == 0) {
                log.error("找不到 [{}] 对应的服务", serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            Instance instance = loadBalance.select(instances);
            return new URL(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务 [{}] 失败：{}", serviceName, e.getMessage());
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
    }
}
