package com.kuney.rpc.hook;

import com.kuney.rpc.registry.util.NacosUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/21 17:50
 */
@Slf4j
public class ShutdownHook {

    private static final ShutdownHook shutdownHook = new ShutdownHook();

    private ShutdownHook() {}

    public static ShutdownHook getInstance() {
        return shutdownHook;
    }

    public void addDeregisterHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtils.deregisterServices();
        }));
    }

}
