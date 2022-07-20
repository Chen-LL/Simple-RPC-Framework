package com.kuney.rpc.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.enums.SerializerCode;
import com.kuney.rpc.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author kuneychen
 * @since 2022/7/20 16:11
 */
@Slf4j
public class KryoSerializer implements Serializer {

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcRequest.class);
        kryo.register(RpcResponse.class);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object o) {
        try (
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Output output = new Output(outputStream);
        ) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(output, o);
            return output.toBytes();
        } catch (Exception e) {
            log.error("Kryo序列化失败：{}", e.getMessage());
            throw new SerializeException("Kryo序列化失败");
        } finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                Input input = new Input(inputStream);
        ) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            return kryo.readObject(input, clazz);
        } catch (Exception e) {
            log.error("Kryo反序列化失败：{}", e.getMessage());
            throw new SerializeException("Kryo反序列化失败");
        } finally {
            KRYO_THREAD_LOCAL.remove();
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.KRYO.getCode();
    }
}
