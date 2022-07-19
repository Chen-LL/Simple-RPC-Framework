package com.kuney.rpc.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kuneychen
 * @since 2022/7/19 17:45
 */
public class SingletonFactory {

    private static final Map<Class<?>, Object> map = new ConcurrentHashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> clazz) {
        Object obj = map.get(clazz);
        if (obj == null) {
            synchronized (clazz) {
                if (obj == null) {
                    try {
                        obj = clazz.newInstance();
                        map.put(clazz, obj);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
        }
        return clazz.cast(obj);
    }
}
