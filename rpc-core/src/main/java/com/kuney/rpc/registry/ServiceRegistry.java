package com.kuney.rpc.registry;

/**
 * @author kuneychen
 * @since 2022/7/12 22:34
 */
public interface ServiceRegistry {

    <T> void register(T service);

    Object getService(String serviceName);

}
