package com.kuney.rpc.hook;

import com.kuney.rpc.util.NacosUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/21 17:50
 */
@Slf4j
public class ShutdownHook {

    private static volatile ShutdownHook shutdownHook;

    private ShutdownHook() {}

    public static ShutdownHook getInstance() {
        if (shutdownHook == null) {
            synchronized (ShutdownHook.class) {
                if (shutdownHook == null) {
                    shutdownHook = new ShutdownHook();
                }
            }
        }
        return shutdownHook;
    }

    public void addDeregisterHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtils.deregisterServices();
        }));
    }

}
