package com.kuney.rpc.serialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuney.rpc.transport.dto.RpcRequest;
import com.kuney.rpc.enums.SerializerCode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author kuneychen
 * @since 2022/7/19 17:53
 */
@Slf4j
public class JsonSerializer implements Serializer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object o) {
        try {
            return objectMapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            log.error("使用Json序列化时出错：{}", e.getMessage());
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object value = objectMapper.readValue(bytes, clazz);
            if (value instanceof RpcRequest) {
                value = handleRequest(value);
            }
            return value;
        } catch (IOException e) {
            log.error("使用Json反序列化时出错：{}", e.getMessage());
            return null;
        }
    }


    /*
        这里由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型，需要重新判断处理
     */
    private Object handleRequest(Object o) throws IOException {
        RpcRequest request = (RpcRequest) o;
        Class<?>[] paramTypes = request.getParamTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> type = paramTypes[i];
            if (!type.isAssignableFrom(request.getParams()[i].getClass())) {
                byte[] bytes = objectMapper.writeValueAsBytes(request.getParams()[i]);
                request.getParams()[i] = objectMapper.readValue(bytes, type);
            }
        }
        return request;
    }

    @Override
    public int getCode() {
        return SerializerCode.JSON.getCode();
    }
}
