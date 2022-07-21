package com.kuney.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.util.NacosUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/20 21:36
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    @Override
    public void register(String serviceName, URL url) {
        try {
            NacosUtils.registerService(serviceName, url);
        } catch (NacosException e) {
            log.error("注册 [{}] 服务失败：{}", serviceName, e.getMessage());
            throw new RpcException(RpcError.FAILED_TO_REGISTER_SERVICE);
        }
    }

}
