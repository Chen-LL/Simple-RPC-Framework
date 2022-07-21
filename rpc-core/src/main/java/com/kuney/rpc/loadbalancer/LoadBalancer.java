package com.kuney.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/21 21:05
 */
public interface LoadBalancer {

    Instance select(List<Instance> instances);

}
