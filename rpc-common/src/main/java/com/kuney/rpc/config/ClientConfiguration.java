package com.kuney.rpc.config;

import com.kuney.rpc.enums.LoadBalancerCode;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/30 17:00
 */
@Slf4j
public class ClientConfiguration extends RpcConfiguration {

    public static int getLoadBalancerCode() {
        String value = properties.getProperty("rpc.loadbalance");
        if (value == null) {
            log.info("使用默认的负责均衡算法：random");
            return LoadBalancerCode.RANDOM.getCode();
        }
        LoadBalancerCode loadBalancerCode = LoadBalancerCode.valueOf(value.toUpperCase());
        if (loadBalancerCode == null) {
            throw new RpcException(RpcError.NOT_SUPPORTED_LOAD_BALANCE_ALGORITHM, ": rpc.loadbalance=" + value);
        }
        log.info("使用用户配置的负载均衡算法：{}", value);
        return loadBalancerCode.getCode();
    }

    public static int getMaxRetryCount() {
        String value = properties.getProperty("rpc.connect.max-retry-count");
        if (value == null) {
            return 5;
        }
        return Integer.parseInt(value);
    }

}
