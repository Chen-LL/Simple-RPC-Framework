package com.kuney.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * @author kuneychen
 * @since 2022/7/21 21:06
 */
public class RandomLoadBalancer implements LoadBalancer {

    private final Random random = new Random();

    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(random.nextInt(instances.size()));
    }
}
