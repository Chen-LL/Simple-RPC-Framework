package com.kuney.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/8/2 21:31
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public Instance select(List<Instance> instances) {
        if (instances == null || instances.size() == 0) {
            return null;
        }
        if (instances.size() == 1) {
            return instances.get(0);
        }
        return doSelect(instances);
    }

    protected abstract Instance doSelect(List<Instance> instances);
}
