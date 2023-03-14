package com.kuney.rpc.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.factory.SingletonFactory;
import com.kuney.rpc.loadbalance.impl.RandomLoadBalance;
import com.kuney.rpc.loadbalance.impl.RoundRobinLoadBalance;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/21 21:05
 */
public interface LoadBalance {

    Instance select(List<Instance> instances);

    static LoadBalance getByCode(int code) {
        switch (code) {
            case 0:
                return SingletonFactory.getInstance(RandomLoadBalance.class);
            case 1:
                return new RoundRobinLoadBalance();
            default:
                throw new RpcException(RpcError.NOT_SUPPORTED_LOAD_BALANCE_ALGORITHM);
        }
    }

}
