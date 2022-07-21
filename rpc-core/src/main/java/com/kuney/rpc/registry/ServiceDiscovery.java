package com.kuney.rpc.registry;

import com.kuney.rpc.entity.URL;

/**
 * @author kuneychen
 * @since 2022/7/21 21:02
 */
public interface ServiceDiscovery {

    URL lookupService(String serviceName);

}
