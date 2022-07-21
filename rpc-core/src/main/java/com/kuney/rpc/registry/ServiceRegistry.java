package com.kuney.rpc.registry;

import com.kuney.rpc.entity.URL;

/**
 * @author kuneychen
 * @since 2022/7/12 22:34
 */
public interface ServiceRegistry {

    void register(String serviceName, URL url);

}
