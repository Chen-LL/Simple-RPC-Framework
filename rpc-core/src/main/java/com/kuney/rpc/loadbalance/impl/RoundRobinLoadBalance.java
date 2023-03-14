package com.kuney.rpc.loadbalance.impl;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kuney.rpc.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于轮询的负载均衡算法
 * @author kuneychen
 * @since 2022/7/21 21:23
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    protected Instance doSelect(List<Instance> instances) {
        int index = atomicInteger.getAndIncrement() % instances.size();
        return instances.get(index);
    }

}
