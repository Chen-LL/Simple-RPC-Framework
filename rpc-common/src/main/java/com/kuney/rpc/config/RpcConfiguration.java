package com.kuney.rpc.config;

import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.enums.SerializerCode;
import com.kuney.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author kuneychen
 * @since 2022/7/25 20:12
 */
@Slf4j
public abstract class RpcConfiguration {

    protected static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = RpcConfiguration.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件失败");
        }
    }

    public static int getSerializerCode() {
        String value = properties.getProperty("rpc.serialize");
        if (value == null) {
            log.info("使用默认的序列化算法：Kryo");
            return SerializerCode.KRYO.getCode();
        }
        SerializerCode serializerCode = SerializerCode.valueOf(value.toUpperCase());
        if (serializerCode == null) {
            throw new RpcException(RpcError.NOT_SUPPORTED_SERIALIZE_ALGORITHM, ": rpc.serialize=" + value);
        }
        log.info("使用用户配置的序列化算法：{}", value);
        return serializerCode.getCode();
    }

}
