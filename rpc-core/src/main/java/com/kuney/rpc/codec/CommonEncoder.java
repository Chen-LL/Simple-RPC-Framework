package com.kuney.rpc.codec;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.enums.PackageType;
import com.kuney.rpc.enums.SerializerCode;
import com.kuney.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/19 17:36
 */
@Slf4j
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final Serializer serializer;

    public CommonEncoder() {
        this.serializer = Serializer.getByCode(SerializerCode.KRYO.getCode());
    }

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
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 4字节魔数，标识是一个协议包
        out.writeInt(MAGIC_NUMBER);
        // 4字节的包类型
        if (msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getType());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getType());
        }
        // 4字节的序列化方式
        out.writeInt(serializer.getCode());
        byte[] data = serializer.serialize(msg);
        // 4字节的数据长度
        out.writeInt(data.length);
        // 数据
        out.writeBytes(data);
    }
}
