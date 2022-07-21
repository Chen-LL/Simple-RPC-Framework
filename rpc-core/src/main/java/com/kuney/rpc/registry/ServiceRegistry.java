package com.kuney.rpc.registry;

import com.kuney.rpc.protocol.URL;

/**
 * @author kuneychen
 * @since 2022/7/12 22:34
 */
public interface ServiceRegistry {

    void register(String serviceName, URL url);

    URL lookupService(String serviceName);

}
