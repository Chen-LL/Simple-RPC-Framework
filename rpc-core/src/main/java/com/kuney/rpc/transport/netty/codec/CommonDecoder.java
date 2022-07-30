package com.kuney.rpc.transport.netty.codec;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.enums.PackageType;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author kuneychen
 * @since 2022/7/19 18:27
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    /*
    +---------------+---------------+-----------------+-------------+
    |  Magic Number |  Package Type | Serializer Type | Data Length |
    |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
    +---------------+---------------+-----------------+-------------+
    |                          Data Bytes                           |
    |                   Length: ${Data Length}                      |
    +---------------------------------------------------------------+
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            log.error("不识别的协议包 MAGIC_NUMBER：{}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int packageType = in.readInt();
        Class<?> packageClass;
        if (packageType == PackageType.REQUEST_PACK.getType()) {
            packageClass = RpcRequest.class;
        } else if (packageType == PackageType.RESPONSE_PACK.getType()) {
            packageClass = RpcResponse.class;
        } else {
            log.error("不识别的数据包 PACKAGE_TYPE：{}", packageType);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        int serializerCode = in.readInt();
        Serializer serializer = Serializer.getByCode(serializerCode);
        if (serializer == null) {
            log.error("不识别的反序列化器 SERIALIZER_CODE：{}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        int length = in.readInt();
        byte[] data = new byte[length];
        in.readBytes(data);
        Object obj = serializer.deserialize(data, packageClass);
        out.add(obj);
    }
}
