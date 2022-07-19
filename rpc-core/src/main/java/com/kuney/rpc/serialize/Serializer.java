package com.kuney.rpc.serialize;

import com.kuney.rpc.factory.SingletonFactory;

/**
 * @author kuneychen
 * @since 2022/7/19 17:40
 */
public interface Serializer {

    byte[] serialize(Object o);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static Serializer getByCode(int code) {
        switch (code) {
            case 1:
                return SingletonFactory.getInstance(JsonSerializer.class);
            default:
                return null;
        }
    }
}
