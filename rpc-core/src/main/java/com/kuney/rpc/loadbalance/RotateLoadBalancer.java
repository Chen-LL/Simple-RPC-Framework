package com.kuney.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 使用轮询算法的负载均衡器
 * @author kuneychen
 * @since 2022/7/21 21:23
 */
public class RotateLoadBalancer implements LoadBalancer {

    private int index;

    @Override
    public Instance select(List<Instance> instances) {
        index %= instances.size();
        return instances.get(index++);
    }
}
