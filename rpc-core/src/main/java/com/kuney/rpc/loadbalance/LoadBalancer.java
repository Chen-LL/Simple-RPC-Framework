package com.kuney.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.factory.SingletonFactory;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/21 21:05
 */
public interface LoadBalancer {

    Instance select(List<Instance> instances);

    static LoadBalancer getByCode(int code) {
        switch (code) {
            case 0:
                return SingletonFactory.getInstance(RandomLoadBalancer.class);
            case 1:
                return new RotateLoadBalancer();
            default:
                throw new RpcException(RpcError.NOT_SUPPORTED_LOAD_BALANCE_ALGORITHM);
        }
    }

}
