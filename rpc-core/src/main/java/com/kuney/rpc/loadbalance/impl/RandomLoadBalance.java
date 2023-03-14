package com.kuney.rpc.loadbalance.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kuney.rpc.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * 基于随机的负载均衡算法
 * @author kuneychen
 * @since 2022/7/21 21:06
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    private final Random random = new Random();

    @Override
    protected Instance doSelect(List<Instance> instances) {
        return instances.get(random.nextInt(instances.size()));
    }
}
